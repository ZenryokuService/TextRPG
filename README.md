# TextRPG
テキストを使用してゲーム展開をするRPGを作ろうとしています。
具体的にはゲームブックをプログラムで再現するような感じです。

### 【目標】

* テキストRPGを作成する、ノンプログラマーがテキストRPGを作成できるように作る。
* Javaプログラミング学習をするときに、特定のクラスを追加することでプログラミングの練習が可能。

### 【制限事項】
プレーヤー、モンスターは1バトルにつき1人(匹)で表現する。複数プレーヤー、複数モンスターに対応していない。

### 【仕様書】
仕様書として[Wiki](https://github.com/ZenryokuService/TextRPG/wiki)を使用します。

* 処理の概要はシーケンス図を参照してください。
* [プログラム仕様はWiki](https://github.com/ZenryokuService/TextRPG/wiki)に追記します。

### 【[UMLドキュメント](https://zenryokuservice.github.io/TextRPG/)】
クラス図、フローチャートをHTML出力しました。上記リンクからアクセスできます。

### メインの処理フロー
![](https://zenryokuservice.github.io/TextRPG/diagrams/b94dd1ecb320dbbb6eacb36f0e181409.svg)

## プログラムの実行方法
1. アプリケーションの起動は、コマンドでJARファイルを実行
2. Swingで作成した画面にテキストを出力、ユーザーからの入力を受けストーリーを進める
3. 途中でゲームを終了するときは、Escボタンを押下する


### 実行イメージ
以下の画面にテキストを出力する
![](http://zenryokuservice.com/wp/wp-content/uploads/2023/01/JTextAreaSize.png)

前回作成したコマンドプロンプト上の実行

[![IMAGE ALT TEXT HERE](https://img.youtube.com/vi/fhvPlw1Jxzk/0.jpg)](https://www.youtube.com/watch?v=fhvPlw1Jxzk)

## 設定ファイル一覧(ゲーム作成者用)
ゲームの作成者は以下のファイルの内容を記述する。サンプルゲームにある、これらのファイルには、簡単な設定が書かれているのでそれをカスタムして利用してほしい。
必須でないもの(必須が「-」)はパス指定(「./XXX.txt」)で説明ファイルを作成することができる。
※パス指定は「./myyFolder/XXX.txtとしてもよい」、つまりフォルダを作成してもよい

| ファイル名 | 記述内容 | 必須 |
| ---------- | -------- | -- |
| <del>title.txt</del> | ゲームを起動したときの初期表示画面(テキストのみで描画する) | ○ |
| [Config.xml](https://github.com/ZenryokuService/TextRPG/wiki/%E8%A8%AD%E5%AE%9A%E5%AE%9A%E7%BE%A9(Config.xml)%E3%81%AB%E9%96%A2%E3%81%97%E3%81%A6) | ゲームの設定、言葉(単語)の意味を定義、HPやMPは固定だが他の値を追加、使用方法を指定する(指定のXMLファイルを作成する)ことができる | ○ |
| [Worlds.xml](https://github.com/ZenryokuService/TextRPG/wiki/%E4%B8%96%E7%95%8C%E8%A6%B3%E3%81%AE%E8%AA%AD%E3%81%BF%E8%BE%BC%E3%81%BF%E5%87%A6%E7%90%86%E3%81%AB%E3%81%A4%E3%81%84%E3%81%A6#%E4%B8%96%E7%95%8C%E8%A6%B3%E3%82%92%E5%AE%9A%E7%BE%A9-worldsxml) | 世界観の定義、各項目を文章で説明する | ○ |
| Countries.xml | 国の定義、各項目を文章で説明する | - |
| Nature.xml | 世界の自然を定義、各項目を文章で説明する | - |
| Creatures.xml | 世界の生物を定義、各項目を文章で説明する| - |
| FoodChain.md | 世界の食物連鎖を定義、各項目を文章で説明する | - |
| Civilzations.xml | 世界の文明を定義、各項目を文章で説明する | - |
| Cultures.xml | 世界の文化を定義、各項目を文章で説明する | - |
| [story_XXX.xml](https://github.com/ZenryokuService/TextRPG/wiki/%E3%82%B9%E3%83%88%E3%83%BC%E3%83%AA%E3%83%BC%E5%AE%9A%E7%BE%A9%E3%81%AB%E3%81%A4%E3%81%84%E3%81%A6) | ゲームのストーリーを記述、各ストーリーのシーンを描画するXMLファイル。「XXX」にはシーン番号が入る。 例: story_1.xml | ○ |
| <del>Story.txt</del> | <del>ゲームのストーリーを記述、ゲームブックの本体のイメージ</del> | <del>-</del> |
| [Job.xml](https://github.com/ZenryokuService/TextRPG/wiki/%E8%81%B7%E6%A5%AD%E5%AE%9A%E7%BE%A9%E3%81%AB%E9%96%A2%E3%81%97%E3%81%A6#jobxml) | 各職業の技、魔法などの習得レベルを記述 | ○ |
| [MonseterType.xml](https://github.com/ZenryokuService/TextRPG/wiki/%E8%81%B7%E6%A5%AD%E5%AE%9A%E7%BE%A9%E3%81%AB%E9%96%A2%E3%81%97%E3%81%A6#monstertypexml) | モンスターの技、魔法などの習得レベルを記述 | ○ |
| [Commands.xml](https://github.com/ZenryokuService/TextRPG/wiki/%E8%81%B7%E6%A5%AD%E5%AE%9A%E7%BE%A9%E3%81%AB%E9%96%A2%E3%81%97%E3%81%A6#commandxml) | 各キャラクターが実行するコマンド| ○ |
| [STM.xml](https://github.com/ZenryokuService/TextRPG/wiki/%E8%81%B7%E6%A5%AD%E5%AE%9A%E7%BE%A9%E3%81%AB%E9%96%A2%E3%81%97%E3%81%A6#stmxml) | 技(Skill, Tech), 魔法(Magic)の効果などを定義 | ○ |
| [Effects.xml])(https://github.com/ZenryokuService/TextRPG/wiki/%E8%81%B7%E6%A5%AD%E5%AE%9A%E7%BE%A9%E3%81%AB%E9%96%A2%E3%81%97%E3%81%A6#effectsxml) | 使用したときに効果のあるもの(どうぐ～魔法など)を実行したときの定義を数式で行う | ○ |
| [Monsters.xml](https://github.com/ZenryokuService/TextRPG/wiki/%E3%83%A2%E3%83%B3%E3%82%B9%E3%82%BF%E3%83%BC%E5%AE%9A%E7%BE%A9%E3%81%AB%E9%96%A2%E3%81%97%E3%81%A6#monstersxml) | モンスターの名前、HPやMPなどを記述する | ○ |
| [Shops.xml](https://github.com/ZenryokuService/TextRPG/wiki/%E5%BA%97%E8%88%97%E5%AE%9A%E7%BE%A9%E3%81%AB%E9%96%A2%E3%81%97%E3%81%A6#shopxml) | ショップの定義、販売しているものと値段のリスト | ○ |
| [Items.xml](https://github.com/ZenryokuService/TextRPG/wiki/%E5%BA%97%E8%88%97%E5%AE%9A%E7%BE%A9%E3%81%AB%E9%96%A2%E3%81%97%E3%81%A6#itemsxml) | アイテム(武具も含む)の定義、販売しているものと値段のリスト、非売品は値段を「-」にする | ○ |
| [Formula.xml](https://github.com/ZenryokuService/TextRPG/wiki/%E8%A8%88%E7%AE%97%E5%BC%8F%E3%81%AE%E5%AE%9A%E7%BE%A9%E3%81%AB%E3%81%A4%E3%81%84%E3%81%A6#formulaxml%E3%81%AE%E5%AE%9A%E7%BE%A9) | 各種計算式を定義する | ○ |
| [map.properties](https://github.com/ZenryokuService/TextRPG/wiki/%E3%83%9E%E3%83%83%E3%83%94%E3%83%B3%E3%82%B0%E3%83%95%E3%82%A1%E3%82%A4%E3%83%AB%E3%81%AB%E9%96%A2%E3%81%97%E3%81%A6#mappingproperties) | XMLファイル間で定義するIDの関連を定義する | ○ |

### 自作XMLの書き方
下のXMLにあるように、Jobクラスがあるので、そのクラスにセットする値を下記のように設定する。
基本的には、サンプルにある設定ファイルをフォルダごとコピーして編集する形で作成してください。
※**詳細の使用は検討中**

### [Config.xml(設定ファイル)](./config/Config.xml)
ゲームで使用する各種パラメータ、ステータス異常の効果を指定する。
```
<?xml version="1.0"?>
<!--
 param:　プレーヤー・モンスターのパラメータ
   .id: 各項目のIDを示す(必須)
   .discription: 各項目の説明(必須)
   .init: 初期値(オプショナル)※←必須ではないということ
   .formula: 各値を求めるための式

 status: プレーヤー・モンスターのステータス異常を表す
   .id: 各項目のIDを示す(必須)
   .discription: 各項目の説明(必須)
   .formula: 状態異常の効果を表す式
-->
<class>
    <param>
        <id>HP</id>
        <discription>生命力・体力を表す、0になるとゲームオーバーになる。</discription>
    </param>
    <param>
        <id>POW</id>
        <discription>ちから: 攻撃力、武器・防具の持てる合計重量を示す。</discription>
        <init>1</init>
    </param>
    <param>
        <id>ATK</id>
        <discription>攻撃力: 物理的な攻撃力を示す。</discription>
        <!-- (ちから + 武器攻撃力) * (1 + (0.1 * じゅくれんど)) -->
        <formula>(POW + WEV) * (1 + (0.1 * JLV))</formula>
    </param>
    <param>
        <id>DEF</id>
        <discription>防御力: 物理的な防御力を示す。</discription>
        <!-- (ちから + すばやさ + きようさ) / 3 + 防具防御力 -->
        <formula>(POW + AGI + DEX) / 3 + ARV</formula>
    </param>
    <status>
        <id>POI</id>
        <discription>毒を受けた状態を表す。</discription>
        <!-- 「@」は毎ターンを表す -->
        <formula>(HP-1)@1</formula>
    </status>
</class>
```
#### Job.xml(職業カスタム)
職業、勇者の職業IDは「BRV」とする。各種IDは3文字で示すようにしている。ここは作成者が決めるところ。。。
共通するタグとして「img」タグがあるので、imgタグで画像を指定することができる。指定しなくてもよい。※必須ではない
自作の職業を増やす場合は、<job>タグの内容をコピーして、新たに職業を作成してください。 
commandListタグに定義されているのは、**コマンド(Command.xml)に定義されているコマンドID**になります。

```xml
<?xml version="1.0"?>
<!-- 職業マップ -->
<class>
    <!-- 勇者 -->
    <job>
        <id>BRV</id>
        <name>勇者</name>
        <discription>勇気ある者。バランスよく剣と魔法を使える。</discription>
        <commands>
        	<command>ATK</command>
        	<command>DEF</command>
        	<command>MAG</command>
        	<command>TEC</command>
        </commands>
        <!-- CONFIG_STSTUS参照のタグ名で増加値をセット -->
        <pow>2</pow>
        <agi>2</agi>
        <int>2</int>
        <dex>2</dex>
        <ksm>2</ksm>
    </job>
    <!-- 戦士 -->
    <job>
        <id>WAR</id>
        <name>戦士</name>
        <discription>戦闘のスペシャリスト。</discription>
        <commands>
        	<command>ATK</command>
        	<command>DEF</command>
        	<command>TEC</command>
        </commands>
        <!-- CONFIG_STSTUS参照のタグ名で増加値をセット -->
        <pow>2</pow>
        <agi>2</agi>
        <int>2</int>
        <dex>2</dex>
        <ksm>2</ksm>
    </job>
</class>
```

### XMLファイルの関連付け
map.propertiesを編集してXMLファイルの関連付けを行う。やり方は以下のようになる。
* Job.xmlの「job」タグ内の「commands」タグ内の「command」タグとCommands.xmlの「coomand」タグ内の「id」タグを関連付ける場合
```job.commands.command=command.id```


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

### 世界観を定義 Worlds.xml
詳細は、[Wikiを参照]()

