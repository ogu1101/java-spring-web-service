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
| Dockerfile | このファイルは、Dockerイメージをビルドするために使用されます。 |
| docker-compose.yml | このファイルは、複数のDockerコンテナを定義し、実行するために使用されます。 |
| .gitignore | このファイルは、Gitの追跡対象から除外したいファイルを設定するために使用されます。 |
| spotbugs-exclude.xml | このファイルは、特定のクラスやメソッドをSpotBugsのバグレポートから除外するために使用されます。 |

Getting Started
---------------

以下の手順は、ローカルPCで開発することを想定したものです。 

このサンプルコードに対して作業を行うには、このリポジトリをローカルPCにcloneする必要があります。 

1. Mavenをインストールしてください。詳細は、 https://maven.apache.org/install.html を参照してください。

1. Dockerをインストールしてください。詳細は、 http://docs.docker.jp/engine/installation/ を参照してください。

1. アプリケーションをビルドしてください。

        $ mvn -f pom.xml -P production compile
        $ mvn -f pom.xml -P production package -DskipTests=true

1. DBコンテナを起動してください。 

        $ docker-compose up -d --build example_db

1. RDBMSの起動完了後にアプリケーションコンテナを起動してください。

        $ docker-compose up -d --build example_app

1. curlコマンドでアプリケーションにHTTPリクエストを送信すると、アプリケーションからHTTPレスポンスが返却されます。

        $ curl -v -X POST -H 'Content-Type:application/json' -d '{"name":"Shuhei Ogura", "emailAddress":"shuhei.ogura@example.com"}' 127.0.0.1:8080/user
        $ curl -v -X GET -H 'Content-Type:application/json' 127.0.0.1:8080/user/1
        $ curl -v -X PUT -H 'Content-Type:application/json' -d '{"name":"Mari Ogura", "emailAddress":"mari.ogura@example.com", "cellPhoneNumber":"09002222222"}' 127.0.0.1:8080/user/1
        $ curl -v -X GET -H 'Content-Type:application/json' 127.0.0.1:8080/user
        $ curl -v -X DELETE -H 'Content-Type:application/json' 127.0.0.1:8080/user/1

What Do I Do Next?
------------------

このアプリケーションは、DBのusersテーブルに対してCRUDを行います。

サンプルコード内の"user"を任意のEntityに置き換えてください。

テストをローカルPCで実行するには、サンプルコードのルートディレクトリに移動して、DBコンテナが起動している状態で `mvn clean compile test` コマンドを実行します。

新しいコードをテストするには、既存のテストクラスを変更するか、src/testディレクトリ配下にテストクラスを追加します。 

CI用レポートを出力するには、サンプルコードのルートディレクトリに移動して、DBコンテナが起動している状態で `mvn clean surefire-report:report checkstyle:checkstyle` コマンドを実行します。
