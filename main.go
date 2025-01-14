package main

import (
	"fmt"
	"os"
	"sync"
	"time"

	"github.com/gin-gonic/gin"
	"go.uber.org/zap"
	"go.uber.org/zap/zapcore"
	"gopkg.in/natefinch/lumberjack.v2"
)

var (
	once           sync.Once
	globalLogger   *zap.Logger
	currentLogDate string
)

// GetLogFileName 生成当前日期的日志文件名
func GetLogFileName() string {
	currentDate := time.Now().Format("2006-01-02")
	return fmt.Sprintf("logs/%s-app.log", currentDate)
}

// GetLumberjackLogger 返回新的 lumberjack.Logger 实例
func GetLumberjackLogger() *lumberjack.Logger {
	return &lumberjack.Logger{
		Filename:   GetLogFileName(),
		MaxSize:    10, // 每个日志文件的最大大小 (MB)
		MaxBackups: 7,  // 保留旧文件的最大个数
		MaxAge:     30, // 保留旧文件的最大天数
		Compress:   true,
	}
}

// GetGlobalLogger 返回全局 logger 实例，并在需要时更新
func GetGlobalLogger() *zap.Logger {
	once.Do(func() {
		encoderConfig := zap.NewProductionEncoderConfig()
		encoderConfig.TimeKey = "time"                        // 时间字段名称
		encoderConfig.EncodeTime = zapcore.RFC3339TimeEncoder // 设置时间格式为 RFC3339

		// 创建 logger
		globalLogger = zap.New(zapcore.NewCore(
			zapcore.NewJSONEncoder(encoderConfig), // 使用自定义编码器
			zapcore.AddSync(GetLumberjackLogger()),
			zap.DebugLevel,
		))
	})

	// 更新日志日期并在必要时重建 logger
	newLogDate := time.Now().Format("2006-01-02")
	if newLogDate != currentLogDate {
		currentLogDate = newLogDate
		// 重新创建 logger
		encoderConfig := zap.NewProductionEncoderConfig()
		encoderConfig.TimeKey = "time"                        // 时间字段名称
		encoderConfig.EncodeTime = zapcore.RFC3339TimeEncoder // 设置时间格式为 RFC3339

		globalLogger = zap.New(zapcore.NewCore(
			zapcore.NewJSONEncoder(encoderConfig), // 使用自定义编码器
			zapcore.AddSync(GetLumberjackLogger()),
			zap.DebugLevel,
		))
	}

	return globalLogger
}

func main() {
	// 创建日志存储目录，如果不存在
	os.MkdirAll("logs", os.ModePerm)

	// 创建 Gin Router
	r := gin.New()

	// 自定义 Gin Logger 中间件
	r.Use(func(c *gin.Context) {
		// 记录请求日志
		GetGlobalLogger().Info("请求", zap.String("method", c.Request.Method), zap.String("path", c.Request.URL.Path))
		c.Next() // 继续处理请求
	})

	// 示例路由，调用全局 logger
	r.GET("/", func(c *gin.Context) {
		GetGlobalLogger().Info("处理 / 请求")
		c.String(200, "Hello World!")
	})

	go func() {
		for {
			GetGlobalLogger().Info("每隔5秒记录一次日志")
			time.Sleep(5 * time.Second)
		}
	}()

	r.Run(":8080") // 默认8080
}
