# SML

## Build

```shell
sh gradlew -Pgroup=com.github.storytellerF clean -xtest assemble :plugin:publishToMavenLocal
```

## 使用

1. 完整复制plugin 文件夹到项目根目录

2. 将导入的plugin 作为composite build 导入项目

3. 在需要的地方添加插件

    ```kts
    id("com.storyteller_f.sml")
    ```

4. 执行`gradlew generateSML`。