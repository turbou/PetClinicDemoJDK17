# PetClinicのJDK17版です。
java17環境へのデプロイ用です。まだ脆弱性は仕込んでいません。

### 前提条件
java, javacともに17となるようにJavaのセットアップ、JAVA_HOMEの環境変数のセットをしておいてください。  

### WARの生成
すぐに使いたい方は [Release](https://github.com/turbou/PetClinicDemoJDK17/releases/latest) ページからビルド済みのwarをダウンロードできます。
```bash
./mvnw -DskipTests -Dcheckstyle.skip clean package
```
target/petclinic.war が出来上がります。

### WARをDockerのTomcat10にデプロイ
Contrastエージェント付きで起動するコマンドです。  
認証情報つきのcontrast.jarをDLしておいてください。  
※適宜、contrast.jarやpetclinic.warのパスは変更してください。  
```bash
docker run -it --rm -p 8881:8080 \
-v $PWD/target/petclinic.war:/usr/local/tomcat/webapps/petclinic.war \
-v $PWD/contrast.jar:/root/contrast.jar \
-e CATALINA_OPTS="$CATALINA_OPTS -javaagent:/root/contrast.jar" \
-e CONTRAST__SERVER__NAME="Tomcat10-JDK17" \
-e CONTRAST__APPLICATION__NAME="PetClinic on Tomcat10" \
tomcat:10.1.17-jre17-temurin
```
http://xxx.xxx.xxx.xxx:8888/petclinic  
でPetClinicが表示されます。  

近々、いつものSQLインジェクションも仕込む予定です。
