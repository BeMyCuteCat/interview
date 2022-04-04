#### interview 面试作业

### 1、方案概述

整体分为controller 层（包括请求参数校验），service 层，repository 层，生产环境使用prod配置文件，采用mysql数据库。开发环境使用dev配置文件，采用H2数据库。单元测试使用test配置文件，采用H2数据库。

scheme.sql创建数据库表。

data.sql 数据库预置数据，即题中要求的Toyota Camry两台，BMW 650两台。

### 2、API 设计

#### 1、客户预订车辆接口

POST
URI：/car
RequestBody 定义：

| 参数     | 类型   | 约束                              | 含义     |
| -------- | ------ | --------------------------------- | -------- |
| userName | String | 非空<br />长度1-256               | 用户名   |
| model    | String | 非空<br />Toyota Camry或者BMW 650 | 车辆型号 |
| num      | int    | 非空<br />大小1-100               | 预订数量 |

ResponseBody定义：
提示性字符串

#### 2、获取所有车辆预定情况接口

GET
URI：/cars
ResponseBody定义：List<CarResponse>

CarResponse

| 参数   | 类型   | 含义         |
| ------ | ------ | ------------ |
| model  | String | 车辆型号     |
| total  | int    | 总数         |
| remain | int    | 库存剩余数量 |
|        |        |              |

#### 3、用户预定情况查询

GET
URI：/user/{name}
约束：name，非空
ResponseBody定义：UserResponse

| 参数   | 类型        | 含义         |
| ------ | ----------- | ------------ |
| name   | String      | 用户名字     |
| models | List<Model> | 车辆预定情况 |

Model

| 参数       | 类型          | 含义     |
| ---------- | ------------- | -------- |
| model      | String        | 车辆型号 |
| num        | int           | 预订数量 |
| createTime | LocalDateTime | 下单时间 |

### 3、github 代码仓地址

https://github.com/BeMyCuteCat/interview

### 4、测试用例

1、模拟HTTP请求，controller、service、repository、H2数据库
2、模拟HTTP请求，controller，模拟service

模拟HTTP具体使用 MockMvc，TestRestTemplate两种方式。

### 5、部署与运行


### 6、客户端

无

