在 idea terminal 工作台下cd 进入 当前项目lib 目录下 执行下面的命令
mvn install:install-file -Dfile=taobao-sdk-java-auto-1479188381469-20200430.jar -DgroupId=com.dingtalk.open -DartifactId=taobao-sdk-java-auto -Dversion=1479188381469-20200430 -Dpackaging=jar


如果使用以上命令打包到本地还是右侧maven 栏这个依赖包报错那就再按以下步骤操作
1.  删除jar包下的  _remote.repositories 文件，然后 mvn  install

2.  第1步不行的话，删除 项目下的  .iml 文件，然后重启idea， 点击 maven 上面的 Reload 再 install


如果不能使用右侧 maven 工具栏打包 使用 那还是在 idea terminal 工作台下执行 mvn install 打包吧 或者看原因