package main

import (
	"fmt"
	"os"
	"time"

	"github.com/gin-gonic/gin"
	"github.com/spf13/viper"
	"go.uber.org/zap"
	"go.uber.org/zap/zapcore"
	"gopkg.in/natefinch/lumberjack.v2"
)

var logger *zap.Logger

func main() {
	// 1. 初始化配置
	initConfig()

	// 2. 初始化日志
	initLogger()

	// 3. 设置Gin模式
	gin.SetMode(viper.GetString("gin.mode"))

	// 4. 创建Gin引擎
	r := gin.New()

	// 5. 使用自定义的日志中间件
	r.Use(GinLogger(logger))

	// 6. 添加一些路由
	r.GET("/", func(c *gin.Context) {
		logger.Info("Handling root request")
		c.JSON(200, gin.H{
			"message": "Hello, World!",
		})
	})

	// 7. 启动服务器
	port := viper.GetString("server.port")
	logger.Info(fmt.Sprintf("Server is starting on port %s", port))
	r.Run(":" + port)
}

func initConfig() {
	viper.SetConfigName("config")
	viper.SetConfigType("yaml")
	viper.AddConfigPath(".")
	err := viper.ReadInConfig()
	if err != nil {
		fmt.Printf("Error reading config file: %s\n", err)
		os.Exit(1)
	}
}

func initLogger() {
	// 配置lumberjack
	lumberjackLogger := &lumberjack.Logger{
		Filename:   viper.GetString("log.filename"),
		MaxSize:    viper.GetInt("log.maxsize"),
		MaxBackups: viper.GetInt("log.maxbackups"),
		MaxAge:     viper.GetInt("log.maxage"),
		Compress:   viper.GetBool("log.compress"),
	}

	// 配置zapcore
	encoderConfig := zap.NewProductionEncoderConfig()
	encoderConfig.EncodeTime = zapcore.ISO8601TimeEncoder

	// 设置日志级别
	logLevel := viper.GetString("log.level")
	var level zapcore.Level
	err := level.UnmarshalText([]byte(logLevel))
	if err != nil {
		fmt.Printf("Error parsing log level: %s\n", err)
		os.Exit(1)
	}

	core := zapcore.NewCore(
		zapcore.NewJSONEncoder(encoderConfig),
		zapcore.AddSync(lumberjackLogger),
		level,
	)

	logger = zap.New(core, zap.AddCaller())
}

func GinLogger(logger *zap.Logger) gin.HandlerFunc {
	return func(c *gin.Context) {
		start := time.Now()
		path := c.Request.URL.Path
		query := c.Request.URL.RawQuery

		c.Next()

		cost := time.Since(start)
		logger.Info(path,
			zap.Int("status", c.Writer.Status()),
			zap.String("method", c.Request.Method),
			zap.String("path", path),
			zap.String("query", query),
			zap.String("ip", c.ClientIP()),
			zap.String("user-agent", c.Request.UserAgent()),
			zap.String("errors", c.Errors.ByType(gin.ErrorTypePrivate).String()),
			zap.Duration("cost", cost),
		)
	}
}
