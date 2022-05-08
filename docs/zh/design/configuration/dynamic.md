# 动态配置下发
## 需求
组件能查询、订阅配置中心的 kv 配置（比如 apollo 中的配置）。

## 产品设计
### User story
1. 用户在 apollo 页面改一下 Redis 的容灾切换配置，Redis 组件就能接收到新配置，把流量切到灾备集群

### 编程界面
允许组件依赖 `config_store` 组件，通过 `config_store` 组件查询、订阅配置数据。
组件之间的依赖关系如图:

![image](https://user-images.githubusercontent.com/26001097/167097668-e5e95eff-9427-4071-bf0a-4138cfe67f90.png)

如果组件有这种需求，需要做以下改动：

1. 组件的配置文件里，添加`import`配置项，声明希望引用的 `config_store` 组件名:
```json
  "state": {
    "redis": {
      "metadata": {
        "redisHost": "localhost:6380",
        "redisPassword": ""
      },
      "import": {
        "config_store": {
          "name": "xxx"
        }
      }
    }
  },
```

2. 组件要实现 `configstores.Setter` interface, 以便 runtime 把`config_store` 注入进去
```go
type Setter interface {
	SetConfigStore(store Store)
}
```

## 方案设计
1. 启动时，优先初始化所有 `config_store` 组件、再创建其他组件
2. runtime 创建组件时，如果组件配置了`import`,则检查组件有没有实现 `Setter` 接口，实现了的话就注入对应的 `config_store` 组件
3. 调组件的 Init 接口，初始化组件

生命周期为：

![image](https://user-images.githubusercontent.com/26001097/167097990-1f675c22-3906-4da9-9eb4-1b12615be1d8.png)
## 其他
1. 有没有密钥动态下发的需求？
