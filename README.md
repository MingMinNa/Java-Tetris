# Tetris
使用 **Java 的 GUI 工具包 Swing** 所做的簡易俄羅斯方塊

## 遊戲操作
* `Space`： 方塊直接下降 （**hard drop**）
*  `←,  →`： 方塊左、右移動 
*  `A,  D`： 方塊左旋和右旋
*  `   ↓   `： 方塊快速下降 （**soft drop**）
## 遊戲畫面
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*遊戲初始畫面(1)*&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*遊戲初始畫面(2)*   
<img src="./img/game_display/HomeScreen_display(1).png" alt="HomeScreen_display(1).png" width="200" height="200">
<img src="./img/game_display/HomeScreen_display(2).png" alt="HomeScreen_display(2).png" width="200" height="200">  

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*GameState1*&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*GameState2*&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*GameState3*  
<img src="./img/game_display/GameScreen_State1.png" alt="GameScreen_State1.png" width="200" height="200">
<img src="./img/game_display/GameScreen_state2.png" alt="GameScreen_state2.png" width="200" height="200">
<img src="./img/game_display/GameScreen_State3.png" alt="GameScreen_State3.png" width="200" height="200">  

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*GameOver*&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*GameClear*  
<img src="./img/game_display/GameOver.png" alt="GameOver.png" width="200" height="200">
<img src="./img/game_display/GameClear.png" alt="GameClear.png" width="200" height="200">  

註：  
(1) 當分數達到 500 分時，進入 State2； 當分數達到 1000 分時，進入 State3  
(2) 消除一行得 50 分，兩行得 150 分，三行得 250 分，四行得 400 分。  
(3) 當分數大於等於 1300 時，遊戲結束(Game Clear)
### 注意事項
1. 本程式使用第三方套件 JLayer（版本 1.0.1），其 JAR 檔案已放置於 lib 資料夾中。
2. 您可以透過修改 music 資料夾中的 MP3 檔案來更換遊戲音樂（請確保使用 MP3 格式）。  
   例如，將欲使用的檔案重新命名為 DeleteLine.mp3，即可更改消除方塊的音效。

### 新增功能
* 新增陰影方塊功能( ghost piece )

