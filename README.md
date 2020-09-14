サンプルWebサービス
==============================================

このアプリケーションは、RDBに対してCRUDを行うシンプルなWebサービスのサンプルです。

このアプリケーションを実装するために使用しているプログラム言語とフレームワークは、以下のとおりです。

| プログラム言語 | フレームワーク |
| :--- | :--- |
| Java 11 | Spring boot 2 |

What's Here
-----------

このサンプルコードには、以下のディレクトリ／ファイルが含まれています。

| ディレクトリ／ファイル | 説明 |
| :--- | :--- |
| README.md | このファイルです。 |
| src/main | このディレクトリには、アプリケーションのソースファイルが含まれています。 |
| src/test | このディレクトリには、アプリケーションのUTファイルが含まれています。 |
| pom.xml | このファイルは、アプリケーションのMavenプロジェクトオブジェクトモデルです。 |
| .gitignore | このファイルは、Gitの追跡対象から除外したいファイルを設定するために使用されます。 |
| spotbugs-exclude.xml | このファイルは、特定のクラスやメソッドをSpotBugsのバグレポートから除外するために使用されます。 |
| .ebextensions/ | このディレクトリには、AWS Elastic Beanstalkがアプリケーションをデプロイするための設定ファイルが含まれています。 |
| buildspec.yml | このファイルは、アプリケーションをビルドするためにAWS CodeBuildで使用されます。 |
| template.yml | このファイルには、AWS CloudFormationがインフラストラクチャをデプロイするために使用するAWSリソースの説明が含まれています。 |
| template-configuration.json | このファイルには、プレースホルダーを含むプロジェクトARNが含まれています。プレースホルダーは、AWSリソースにプロジェクトIDをタグ付けするために使用されます。 |

このアプリケーションをAWSクラウド以外の環境にデプロイする場合、以下のディレクトリ／ファイルは、不要となります。
  
- .ebextensions/
- buildspec.yml
- template.yml
- template-configuration.json

Getting Started
---------------

以下の手順は、ローカルPCで開発することを想定したものです。 

このサンプルコードに対して作業を行うには、このリポジトリをローカルPCにcloneする必要があります。 

1. Mavenをインストールしてください。詳細は、 https://maven.apache.org/install.html を参照してください。

1. Tomcatをインストールしてください。詳細は、 https://tomcat.apache.org/tomcat-9.0-doc/setup.html を参照してください。

1. MySQLをインストールしてください。詳細は、 https://dev.mysql.com/doc/refman/8.0/en/installing.html を参照してください。

1. application.propertiesの記載内容に合わせて、DBとDBユーザーを作成してください。
   DBテーブルは、アプリケーションの起動時に自動作成されます。

1. アプリケーションをビルドしてください。

        $ mvn -f pom.xml compile
        $ mvn -f pom.xml package

1. ビルド成果物であるROOT.warをTomcatのwebappディレクトリにコピーしてください。 

        $ cp target/ROOT.war <tomcat webapp directory>

1. Tomcatサーバーを再起動してください。

1. curlコマンドでアプリケーションにHTTPリクエストを送信すると、アプリケーションからHTTPレスポンスが返却されます。

        $ curl -v -X POST -H 'Content-Type:application/json' -d '{"name":"Shuhei Ogura", "emailAddress":"shuhei.ogura@example.com"}' localhost:8080/user
        $ curl -v -X GET -H 'Content-Type:application/json' localhost:8080/user/1
        $ curl -v -X PUT -H 'Content-Type:application/json' -d '{"name":"Mari Ogura", "emailAddress":"mari.ogura@example.com", "cellPhoneNumber":"09002222222"}' localhost:8080/user/1
        $ curl -v -X GET -H 'Content-Type:application/json' localhost:8080/user
        $ curl -v -X DELETE -H 'Content-Type:application/json' localhost:8080/user/1

What Do I Do Next?
------------------

このアプリケーションは、DBのusersテーブルに対してCRUDを行います。

サンプルコード内の"user"を任意のEntityに置き換えてください。

テストをローカルPCで実行するには、サンプルコードのルートディレクトリに移動して、 `mvn clean compile test` コマンドを実行します。

新しいコードをテストするには、既存のテストクラスを変更するか、src/testディレクトリ配下にテストクラスを追加します。 

CI用レポートを出力するには、サンプルコードのルートディレクトリに移動して、 `mvn clean surefire-report:report checkstyle:checkstyle` コマンドを実行します。
