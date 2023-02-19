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

| ファイル名 | 記述内容 | 必須 |
| ---------- | -------- | -- |
| title.txt | ゲームを起動したときの初期表示画面(テキストのみで描画する) | ○ |
| Config.xml | ゲームの設定、言葉(単語)の意味を定義、HPやMPは固定だが他の値を追加、使用方法を指定する(指定のXMLファイルを作成する)ことができる | ○ |
| Worlds.xml | 世界観の定義、各項目を文章で説明する | ○ |
| Countries.xml | 国の定義、各項目を文章で説明する | - |
| Nature.xml | 世界の自然を定義、各項目を文章で説明する | - |
| Creatures.xml | 世界の生物を定義、各項目を文章で説明する | - |
| FoodChain.md | 世界の食物連鎖を定義、各項目を文章で説明する | - |
| Civilzations.xml | 世界の文明を定義、各項目を文章で説明する | - |
| Cultures.xml | 世界の文化を定義、各項目を文章で説明する | - |
| story_XXX.xml | ゲームのストーリーを記述、各ストーリーのシーンを描画するXMLファイル。「XXX」にはシーン番号が入る。 例: story_1.xml | ○ |
| <del>Story.txt</del> | <del>ゲームのストーリーを記述、ゲームブックの本体のイメージ</del> | <del>-</del> |
| Job.xml | 各職業の技、魔法などの習得レベルを記述 | ○ |
| MonseterType.xml | モンスターの技、魔法などの習得レベルを記述 | ○ |
| Commands.xml | 各キャラクターが実行するコマンド| ○ |
| STM.xml | 技(Skill, Tech), 魔法(Magic)の効果などを定義 | ○ |
| Effects.xml | 使用したときに効果のあるもの(どうぐ～魔法など)を実行したときの定義を数式で行う | ○ |
| Monsters.xml モンスターの名前、HPやMPなどを記述する | ○ |
| Shops.xml | ショップの定義、販売しているものと値段のリスト | ○ |
| Items.xml | アイテム(武具も含む)の定義、販売しているものと値段のリスト、非売品は値段を「-」にする | ○ |
| Formula.xml | 各種計算式を定義する | ○ |
| map.properties | XMLファイル間で定義するIDの関連を定義する | ○ |

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
ここで定義した情報は、HELPで表示することができる。(仮)
ストーリーの表示するときにも、ここの定義を引用することが可能、またイメージ画像があるともっと良いので「img」タグで画像を追加することが可能。

##### **name**: 世界の名前
 * 世界地図(画像ファイル)

##### **nature**: 自然
 * モンスターを含む動植物には食物連鎖
 * 生息地、生物分布
 * 地形、天候、四季(雨季と乾季など)
 * 魔法(の類)の発動ロジック、効果、特性

##### **civilzation**: 世界の文明
 * 生活様式全般
 * 生活習慣
 * 価値観
 * 世界観
 * 規範
 * 思考様式
 * 社会制度
 * 社会構造
 * 組織

##### **culture**: 文化
 * 思考
 * 感情
 * 衣
 * 食
 * 住
 * 機械(道具)
 * 制度
 * 言語
 * 社会体制
 
 ##### **country**: 国(これを基本単位にする)
  * 文明圏
  * 文化
  * 自然
  * 国家(国家の名前がないときは国家なし)
  * 国家制度(封建・民主主義・社会主義・その他)

#### XMLのサンプル　※検討中
```
<?xml version="1.0"?>
<!-- 世界観 -->
<class>
    <world>
        <!-- name: 世界の名前、何もない場合は空でよい-->
        <name></name>
        <!-- 世界地図(画像ファイル)の画像URL -->
        <img>[画像へのURLもしくはパス](https://img.altema.jp/dq3/uploads/2020/08/2020y08m30d_1317524902.jpg)/img>
        <!-- nature: 自然 -->
        <nature>
            <climate>
                <id>Et</id>
                <name>寒帯気候(かんたいきこう)</name>
                <creatures>
                    <creature id="tonakai"/>
                    <creature id="arcticfox"/>
                    <cresture id="grizzlybear"/>
                </creatures>
                <!-- 定義は自由に追加してよい -->
            </climate>
            <climate>
                <id>Dw</id>
                <name>亜寒帯気候(あかんたい きこう)</name>
                <creatures>
                    <creature id="amurleopard"/>
                </creatures>
            </climate>
            <climate>
                <id>Cｗ</id>
                <name>温帯気候(おんたいきこう)</name>
                <creatures>
                    <creature id="lynx"/>
                    <creature id="eagle"/>
                </creatures>
            </climate>

            <climate>
                <id>ＢＷ</id>
                <name>乾燥帯気候(かんそうたい きこう)</name>
                <creatures>
                    <creature>
                        <id>camel</id>
                        <name>ラクダ</name>
                        <discription>ラクダは、楽だ</discription>
                    </creature>
                </creatures>
            </climate>

            <climate>
                <id>Af</id>
                <name>熱帯雨林気候</name>
                <discription>ＴＨＥ高温多湿！</discription>
                <creatures>./Creature.xml</creatures>
            </climate>

        </nature>
        <!-- モンスターを含む動植物の食物連鎖 -->
        <food_chain>
            植物→草食動物→肉食動物→雑食動物
        </food_chain>
        <food_chain>
            <!-- 連鎖になっていない場合, 要は「食う、食われるの関係がわかればよい-->
            植物、獣人→昆虫→鳥、爬虫類
        </food_chain>
        <foot_chain>
            植物、獣→不死
        </food_chain>
        <!-- 詳細を記したファイルを指定してもよい。 -->
        <food_chain>./FoodChain.md</food_chain>
        <!-- 生息地、生物分布 -->
        <!-- 地形、天候、四季(雨季と乾季など) -->
        <!-- 魔法(の類)の発動ロジック、効果、特性 -->
        <!-- 世界の文明 -->
        <civilizations>
            <civilization>
                <id>indas</id>
                <name>インダス</name>
                <discription>インダス川の流域に発展した文明</discription>
            </civilization>
            <civilization>
                <id>koga</id>
                <name>黄河</name>
                <!-- koga.txtファイルに詳細を記載 -->
                <discription>./civilizations/koga.txt</discription>
            </civlization>
            <!-- 属性部分にidと名前を指定する場合の書き方(これは、詳細を記述しない場合に使用する。 -->
            <civilization id="methopotamia" name="メソポタミア" />
            <civilization id="egypt" name="エジプト" />
        </civilizations>
        <!-- 生活様式全般 -->
        <!-- 生活習慣 -->
        <!-- 価値観 -->
        <!-- 世界観 -->
        <!-- 規範 -->
        <!-- 思考様式 -->
        <!-- 社会制度 -->
        <!-- 社会構造 -->
        <!-- 組織 -->
        <!-- culture: 文化 -->
        <!-- 思考 -->
        <!-- 感情 -->
        <!-- 衣 -->
        <!-- 食 -->
        <!-- 住 -->
        <!-- 機械(道具) -->
        <!-- 制度 -->
        <!-- 言語 -->
        <!-- 社会体制 -->
    </world>
</class>   
```
