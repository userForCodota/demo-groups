// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract SimpleContract {
    // 状态变量
    uint256 public number;
    address public owner;

    // 枚举
    enum Status {Inactive, Active, Suspended}
    Status public status;

    // 结构体
    struct Person {
        string name;
        uint256 age;
    }
    Person public person;

    // 事件
    event NumberUpdated(uint256 newNumber);
    event StatusChanged(Status newStatus);

    // 错误
    error Unauthorized();

    // 函数修饰器
    modifier onlyOwner() {
        if (msg.sender != owner) {
            revert Unauthorized();
        }
        _;
    }

    // 构造函数
    constructor() {
        owner = msg.sender;
        status = Status.Inactive;
    }

    // 函数
    function setNumber(uint256 _number) public onlyOwner {
        number = _number;
        emit NumberUpdated(_number);
    }

    function setStatus(Status _status) public onlyOwner {
        status = _status;
        emit StatusChanged(_status);
    }

    function setPerson(string memory _name, uint256 _age) public onlyOwner {
        person = Person(_name, _age);
    }
}
