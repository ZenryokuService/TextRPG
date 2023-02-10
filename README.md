# TextRPG
【目標】

* テキストRPGを作成する、ノンプログラマーがテキストRPGを作成できるように作る。
* Javaプログラミング学習をするときに、特定のクラスを追加することでプログラミングの練習が可能。

【制限事項】
プレーヤー、モンスターは1バトルにつき1人(匹)で表現する。複数プレーヤー、モンスターに対応していない。


## プログラムの構成
1. アプリケーションの起動は、コマンドでJARファイルを実行
2. Swingで作成した画面にテキストを出力、ユーザーからの入力を受ける

### 実行イメージ
以下の画面にテキストを出力する
![](http://zenryokuservice.com/wp/wp-content/uploads/2023/01/JTextAreaSize.png)

前回作成したコマンドプロンプト上の実行

[![IMAGE ALT TEXT HERE](https://img.youtube.com/vi/fhvPlw1Jxzk/0.jpg)](https://www.youtube.com/watch?v=fhvPlw1Jxzk)

## 設定ファイル一覧(ゲーム作成者用)
ゲームの作成者は以下のファイルの内容を記述する。サンプルゲームにある、これらのファイルには、簡単な設定が書かれているのでそれをカスタムして利用してほしい。

| ファイル名 | 記述内容 |
| ---------- | -------- |
| title.txt | ゲームを起動したときの初期表示画面(テキストのみで描画する) |
| conf.txt | ゲームの設定、言葉(単語)の意味を定義、HPやMPは固定だが他の値を追加、使用方法を指定することができる |
| story_XXX.xml | ゲームのストーリーを記述、各ストーリーのシーンを描画するXMLファイル。「XXX」にはシーン番号が入る。 例: story_1.xml |
| <del>Story.txt</del> | <del>ゲームのストーリーを記述、ゲームブックの本体のイメージ</del> |
| Job.xml | 各職業の技、魔法などの習得レベルを記述 |
| Commands.xml | 各キャラクターが実行するコマンド|
| STM.xml | 技(Skill, Tech), 魔法(Magic)の効果などを定義 |
| MonseterType.xml | モンスターの技、魔法などの習得レベルを記述 |
| Monsters.xml モンスターの名前、HPやMPなどを記述する |
| Shops.xml | ショップの定義、販売しているものと値段のリスト |
| Items.xml | アイテム(武具も含む)の定義、販売しているものと値段のリスト、非売品は値段を「-」にする |


## Sceneの種類
| No | シーンタイプ | シーンの役割 |
| -- | ----------- | ----------- |
| 1 | ストーリーシーン | XMLファイルに定義されているstoryタグの内容を表示する |
| 2 | バトルシーン    | XMLファイルに定義されているmonsterタグの内容を表示する |
| 3 | プレーヤー生成シーン | プレーヤーを作成するシーン |
| 4 | 次のシーンを選択するシーン | 次のシーン番号を指定してそのシーンを呼び出す |
| 5 | ショップシーン | 買い物を行うシーン|
| 6 | エフェクトシーン | 「お金を拾う」「HPが回復した」などのプレーヤーに対する効果を起こすシーン |

それぞれのシーンを実装するのに、前回のシーンでは、テキストファイルからタグをつけてシーンタイプを指定していたため
ユーザーが作成する[ストーリーに対し大きな制限](https://github.com/ZenryokuService/ObjectOrientedPrograming/tree/master/src/main/java/jp/zenryoku/rpg/scene)があった。
これを、XMLファイルに変更して次のように実装する

### XMLでのシーン定義
タグでシーンの動きを定義するようにする。

#### 1.storyタグ
「ストーリーシーン」を実行する。
単純にタグ内の文字列を表示する。

#### 2.batlleタグ
「バトルシーン」を実行する。
戦闘開始前の文言、例えば「XXXが現れた！」、を定義。モンスターIDを指定し、Monster.xmlから対象のモンスターを取得する。

#### 3.create
「プレーヤー生成シーン」を実行する。もしくは、プレーヤーを初めから用意しておく。
1. 名前から性別などを入力して、生成する(設定ファイルにて必要な入力項目を定義するようにする)
2. プレーヤーの名前から性別、ステータスを定義、職業の類もセットできるようにする。


#### 4.nextタグ
次のシーンを選択するシーンを実行する。
選択肢と次のシーン番号をセットにして表示、選択する。

#### 5.effectタグ
「エフェクトシーン」を実行する。
「conf.txt」で定義しているパラメータをプレーヤーに対して上げたり下げたりする。
例：「プレーヤーは10のダメージを受けた！」、「100円拾った」、「たろう(犬)がなついてきた(効果なし)」など

#### 6.shopタグ
「ショップシーン」を実行する。
ショップIDで指定した店を表示、買い物など利用することができる。「宿屋」などはeffectタグをつけることで何かしらの効果を起こすことができる
