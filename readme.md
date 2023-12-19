# PetClinicのJDK11版です。
java11環境へのデプロイとか、gaeへのデプロイなどの手順を追加していきます。

### Springブート起動
```bash
./mvnw -DskipTests spring-boot:run
```

### GAEへのデプロイ
```bash
./mvnw -DskipTests package appengine:deploy
```
