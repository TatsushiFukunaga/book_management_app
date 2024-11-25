# 書籍管理システム - バックエンドAPI

このプロジェクトは、書籍管理システムのバックエンドAPIです。以下の機能を提供します。

- 書籍と著者の情報を登録・更新
- 著者に紐づく書籍情報の取得

## 技術スタック

- **言語**: Kotlin
- **フレームワーク**: Spring Boot, jOOQ
- **データベース**: PostgreSQL
- **その他ツール**: Flyway (DBマイグレーション), Docker, Docker Compose

## 機能概要

### 書籍の属性
- タイトル
- 価格 (0以上であること)
- 著者 (最低1人以上)
- 出版状況
  - 未出版 (`UNPUBLISHED`)
  - 出版済み (`PUBLISHED`)
  - ※ 出版済みステータスから未出版に変更することは不可

### 著者の属性
- 名前 (空白不可)
- 生年月日 (現在の日付より過去であること)
- 複数の書籍を執筆可能

---

## セットアップ手順

### 1. 必要なツールをインストール
- **JDK**: バージョン 17 以上
- **Gradle**: 推奨 Gradle Wrapper を使用 (`./gradlew`)
- **Docker**: 最新版
- **IntelliJ IDEA**: 推奨 IDE

### 2. プロジェクトのクローン
```bash
git clone https://github.com/your-username/book-management-app.git
cd book-management-app
```

### 3. 環境構築

必要に応じて、application.yml の設定を更新します。
PostgreSQL を Docker Compose で起動します:
```bash
docker-compose up -d
```

### 4. データベースのマイグレーション
以下のコマンドを実行してデータベースを初期化します。
```bash
./gradlew flywayMigrate
```

### 5. アプリケーションの起動
```bash
./gradlew bootRun
```
アプリケーションはデフォルトで http://localhost:8080 で起動します。

---

## APIエンドポイント

### 著者API

| HTTPメソッド | URL              | 機能                     |
|--------------|------------------|--------------------------|
| GET          | `/authors`       | 全著者を取得             |
| GET          | `/authors/{id}`  | 指定されたIDの著者を取得 |
| POST         | `/authors`       | 新しい著者を作成         |
| PUT          | `/authors/{id}`  | 著者情報を更新           |
| DELETE       | `/authors/{id}`  | 著者を削除               |

---

### 書籍API

| HTTPメソッド | URL              | 機能                     |
|--------------|------------------|--------------------------|
| GET          | `/books`         | 全書籍を取得             |
| GET          | `/books/{id}`    | 指定されたIDの書籍を取得 |
| GET          | `/books/{id}/books`    | 指定された著者の書籍を取得 |
| POST         | `/books`         | 新しい書籍を作成         |
| PUT          | `/books/{id}`    | 書籍情報を更新           |
| DELETE       | `/books/{id}`    | 書籍を削除               |

---

## テスト
### 単体テストの実行
以下のコマンドで全テストを実行します:
```bash
./gradlew test
```

### テストレポートの確認
build/reports/tests/test/index.html をブラウザで開いて結果を確認してください。

