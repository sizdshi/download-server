# download-server

### Web
> Node.js >= 16

```bash
cd web
yarn
npm run dev
```
### Server
1. Install maven, add mvn to PATH (Linux/Windows)
2. Setup aliyun mirrors with `~/.m2/settings.xml`
```xml
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">

        <mirrors>
                <mirror>
                    <id>aliyunmaven</id>
                    <mirrorOf>*</mirrorOf>
                    <name>aliyun public maven</name>
                    <url>https://maven.aliyun.com/repository/public</url>
                </mirror>
        </mirrors>
</settings>
```
3. run server
```bash
cd server
mvn spring-boot:run
```
