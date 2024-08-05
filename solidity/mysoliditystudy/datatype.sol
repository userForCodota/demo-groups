// SPDX-License-Identifier: GPL-3.0

pragma solidity ^0.8.17;

contract BoolShortCircuit {
    address addr = 0x690B9A9E9aa1C9dB991C7721a92d351Db4FaC990;

    uint256 public zeroCount = 0;

    function isEven(uint256 num) private pure returns (bool) {
        return num % 2 == 0;
    }

    // isZero函数有副作用，会改变状态变量zeroCount的值
    function isZero(uint256 num) private returns (bool) {
        if (num == 0) {
            zeroCount += 1; // 函数副作用，会改变zeroCount的值
        }
        return num == 0;
    }

    function get_balance() public view returns (uint256) {
        return address(this).balance; //获取地址账户余额
    }

    function get_code() public view returns (bytes memory) {
        return address(this).code; //获取合约代码
    }

    function get_codehash() public view returns (bytes32) {
        return address(this).codehash; //获取合约代码的hash值
    }
}
