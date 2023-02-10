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
| Config.xml | ゲームの設定、言葉(単語)の意味を定義、HPやMPは固定だが他の値を追加、使用方法を指定する(指定のXMLファイルを作成する)ことができる |
| Worlds.xml | 世界観の定義、各項目を文章で説明する |
| story_XXX.xml | ゲームのストーリーを記述、各ストーリーのシーンを描画するXMLファイル。「XXX」にはシーン番号が入る。 例: story_1.xml |
| <del>Story.txt</del> | <del>ゲームのストーリーを記述、ゲームブックの本体のイメージ</del> |
| Job.xml | 各職業の技、魔法などの習得レベルを記述 |
| Commands.xml | 各キャラクターが実行するコマンド|
| STM.xml | 技(Skill, Tech), 魔法(Magic)の効果などを定義 |
| MonseterType.xml | モンスターの技、魔法などの習得レベルを記述 |
| Monsters.xml モンスターの名前、HPやMPなどを記述する |
| Shops.xml | ショップの定義、販売しているものと値段のリスト |
| Items.xml | アイテム(武具も含む)の定義、販売しているものと値段のリスト、非売品は値段を「-」にする |

### 自作XMLの書き方
下のXMLにあるように、Jobクラスがあるので、そのクラスにセットする値を下記のように設定する。
例：職業、勇者の職業IDは「BRV」とする。各種IDは3文字で示すようにしている。ここは作成者が決めるところ。。。
　　共通するタグとして「img」タグがあるので、imgタグで画像を指定することができる。指定しなくてもよい。※必須ではない
```xml
<?xml version="1.0"?>
<!-- 職業マップ -->
<class>
    <!-- 勇者 -->
    <job>
        <id>BRV</id>
        <name>勇者</name>
        <discription>勇気ある者。バランスよく剣と魔法を使える。</discription>
        <commandList>ATK, DEF, MAG, TEC</commandList>
        <!-- CONFIG_STSTUS参照のタグ名で増加値をセット -->
        <pow>2</pow>
        <agi>2</agi>
        <int>2</int>
        <dex>2</dex>
        <ksm>2</ksm>
        <img>resources/img/brv.png</img>
    </job>
    <!-- 戦士 -->
    <job>
        <id>WAR</id>
        <name>戦士</name>
        <discription>戦闘のスペシャリスト。</discription>
        <commandList>ATK, DEF, TEC</commandList>
        <!-- CONFIG_STSTUS参照のタグ名で増加値をセット -->
        <pow>2</pow>
        <agi>2</agi>
        <int>2</int>
        <dex>2</dex>
        <ksm>2</ksm>
    </job>
</class>
```
#### XMLの具体例
```
<?xml version="1.0"?>
<!-- これはコメント -->
<class>
  <!-- Config.xmlに定義している値を定義 -->
  <!-- 「POW＝ ちから」と定義しているのでちからが5ポイントならば -->
  <!-- 大文字で定義、小文字で使用する感じ -->
  <pow>5</pow>
</class>
```

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
```
<?xml version="1.0"?>
<!-- 世界観 -->
<class>
    <world>
        <!-- name: 世界の名前 -->
        <name>ちきゅう</name>
        <!-- 世界地図(画像ファイル) -->
        <img>https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTYR-1XE6ThYpnNcdE0Rt55fKVfMOkZQdVhpg&usqp=CAU</img>
        <!-- nature: 自然 -->
        <nature>
            <climate>
                <name>寒帯気候(かんたいきこう)</name>
                <creatures>
                    <creature id="tonakai"/>
                    <creature id="arcticfox"/>
                    <cresture id="grizzlybear"/>
                </creatures>
                <!-- 定義は自由に追加してよい -->
            </climate>
            <climate>
                <name>亜寒帯気候(あかんたい きこう)</name>
                <creatures>
                    <creature id="amurleopard"/>
                </creatures>
            </climate>
            <climate>
                <name>温帯気候(おんたいきこう)</name>
                <creatures>
                    <creature id="lynx"/>
                    <creature id="eagle"/>
                </creatures>
            </climate>

            <climate>
                <name>乾燥帯気候(かんそうたい きこう)</name>
                <creatures>
                    <creature>
                        <id>camel</id>
                        <name>ラクダ</name>
                        <discription>ラクダは、楽だ</discription>
                    </creature>
                </creatures>
            </climate>

            <climate></climate>

        </nature>
        <!-- モンスターを含む動植物には食物連鎖 -->
        <!-- 生息地、生物分布 -->
        <!-- 地形、天候、四季(雨季と乾季など) -->
        <!-- 魔法(の類)の発動ロジック、効果、特性 -->
        <!-- civilzation: 世界の文明 -->
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
