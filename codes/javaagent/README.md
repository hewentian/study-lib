### 在目标 jar 包运行之前，执行 premain 方法
``` bash
$ cd /home/hewentian/ProjectD/gitHub/study-lib/codes/javaagent
$ mvn clean package
$ java -javaagent:pre-main/target/pre-main.jar -jar hello/target/hello.jar
```

### 在目标 jar 包运行之后，执行 agentmain 方法，实现在不重启目标 jar 包的情况下，重写目标 jar 包的类
``` bash
$ cd /home/hewentian/ProjectD/gitHub/study-lib/codes/javaagent
$ mvn clean package
$ java -jar hello/target/hello.jar
$ java -Xbootclasspath/a:$JAVA_HOME/lib/tools.jar -jar agent-main-test/target/agent-main-test.jar
```
