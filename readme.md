# PetClinicのJDK17版です。
java1７環境へのデプロイ用です。まだ脆弱性は仕込んでいません。

### WARの生成
```bash
./mvnw -DskipTests
```
target/petclinic.war が出来上がります。

### WARをDockerのTomcat10にデプロイ
Contrastエージェント付きで起動するコマンドです。  
認証情報つきのcontrast.jarをDLしておいてください。  
※適宜、contrast.jarやpetclinic.warのパスは変更してください。  
```bash
docker run -it --rm -p 8888:8080 \
-v /root/git/PetClinicDemoJDK17/target/petclinic.war:/usr/local/tomcat/webapps/petclinic.war \
-v /root/contrast.jar:/root/contrast.jar \
-e CATALINA_OPTS="$CATALINA_OPTS -javaagent:/root/contrast.jar" \
tomcat:10.1.17-jre17-temurin
```
http://xxx.xxx.xxx.xxx:8888/petclinic  
でPetClinicが表示されます。  

近々、いつものSQLインジェクションも仕込む予定です。
