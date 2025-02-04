int layer_num, score, playerX, playerY,
  player_speed, acornX, acornY, acorns, power;
int[] layers = {750, 675, 600, 525, 450, 375, 300, 225, 150, 75, 0};
int[] layerColor = new int[11];
int[] layerOutline = new int[11];

float layer_type, greens;
float[] boatX = new float[10];
float[] boatSpeed = new float[10];
float[] wolfX = new float[10];
float[] wolfSpeed = new float[10];

color green = color(120, 200, 90), gray = color(110, 110, 110), blue = color(30, 160, 220),
  darkblue = color(20, 110, 140), darkgreen = color(80, 140, 60), white = color(255), red = color(200, 30, 30);

PImage player_img, boat_img, wolf_img, acorn_img;

boolean startGame = false;
boolean[] SpawnObstacle = new boolean[10];
boolean[] SpawnLog = new boolean[10];
boolean[] Direction = new boolean[10];

String game, acorn_msg, power_msg, score_text, total_text, powerup;

void setup() { // Set up innital variables values here so we can reset the game easier
  size(790, 825);
  player_img = loadImage("squirrel.png");
  player_img.resize(50, 50);
  layerColor[0] = green;
  layerColor[10] = green;
  greens = 1.2;
  layer_num = 750;
  player_speed = 20;
  playerX = 400;
  playerY = 775;
  acorns = 0;
  score = 0;
  powerup = null;
  resetLayers();
}

void draw() {
  if (!startGame) { // if game has not started
    background(green);
    fill(0);
    textSize(100);
    text("CROSSY SQUIRREL", 10, 100);
    textSize(50);
    text("Hit ENTER to Start", 200, 300);  // print rules and how to start game
    text(" Use ↑ to move forward", 150, 400);
    text(" Use ↓ to move backward", 150, 450);
    text(" Use ← to move left", 150, 500);
    text(" Use → to move right", 150, 550);
  } else {
    for (int i = 0; i <= 10; i++) { // print the layers 
      fill(layerColor[i]);
      stroke(layerOutline[i]);
      rect(0, layers[i], width, 75);
    }
    spawnBoat(); // draw player and call all methods
    spawnWolf();
    image(player_img, playerX, playerY);
    noFill();
    stroke(darkblue);
    rect(playerX, playerY, 50, 50); // add player outline if wanted you can comment out
    checkCollision();
    game();
    acorn();
  }
}

void keyPressed() {
  if (keyCode == ENTER) {
    if (!startGame) { // if enter is pressed start game
      startGame = true;
    } else {
      setup(); // Restart the game
    }
  }

  if (startGame) { // Allow player movement only after the game starts
    if (keyCode == UP) { // moves player forward
      playerY -= player_speed;
    }
    if (playerY < 775 && keyCode == DOWN) { // moves player backward and cant go under screen
      playerY += player_speed;
    }
    if (playerX > 1 && keyCode == LEFT) { // moves player left and cant go left if going off screen
      playerX -= player_speed;
    }
    if (playerX < 740 && keyCode == RIGHT) { // moves player right and cant go right if going off screen
      playerX += player_speed;
    }
  }
}

void checkCollision() {
  for (int i = 0; i < 10; i++) {
    if (SpawnLog[i] && playerX + 50 > boatX[i] && playerX < boatX[i] + 150 &&
      playerY + 50 > layers[i] && playerY < layers[i] + 75) { // if player hits boat
      if (Direction[i]) { // makes player follow the boat depending on direction
        playerX += boatSpeed[i]; //makes the player and boat move together
      } else {
        playerX -= boatSpeed[i]; //makes the player and boat move together
      }
    } else if (layerColor[i] == blue && playerX >= 0 && playerX + 50 <= width &&
      playerY >= layers[i] && playerY + 50 <= layers[i] + 75) {
      end(); // if player is not touching boat and is in the water
    }
    if (SpawnObstacle[i] && playerX + 50 >= wolfX[i] && playerX <= wolfX[i] + 120 &&
      playerY + 50 >= layers[i] && playerY <= layers[i] + 75) {
      end(); // if the player hits the wolf the game ends
    }
  }
}

void spawnBoat() {// generates the boat
  for (int i = 0; i < 10; i++) { 
    if (SpawnLog[i]) { // check if layer is blue
      noFill();
      stroke(darkblue);
      rect(boatX[i], layers[i], 150, 75);  // add boat outline if wanted you can comment out
      if (Direction[i]) {  // whether the boat is going left or right
        boat_img = loadImage("Boat.png");
        boat_img.resize(150, 75);
        image(boat_img, boatX[i], layers[i]);
        boatX[i] += boatSpeed[i]; // makes the boat move
      } else {
        boat_img = loadImage("Boat_Left.png"); // make boat visualie face left
        boat_img.resize(150, 75);
        image(boat_img, boatX[i], layers[i]);
        boatX[i] -= boatSpeed[i]; // makes the boat move
      }
    }
    if (boatX[i] >= 1000 || boatX[i] <= -200) {
      Direction[i] = !Direction[i]; // turn around
    }
  }
}

void spawnWolf() {// spawns the wolf
  for (int i = 0; i < 10; i++) {
    if (SpawnObstacle[i]) { // if the layer is a road
      noFill();
      stroke(red);
      rect(wolfX[i], layers[i], 120, 75); // add wolf outline if wanted you can comment out
      if (Direction[i]) { // wether the wolf is going left or right
        wolf_img = loadImage("Wolf.png"); // has the wolf facing right
        wolf_img.resize(120, 75);
        image(wolf_img, wolfX[i], layers[i]);
        wolfX[i] += wolfSpeed[i]; // makes the wolf move
      } else {
        wolf_img = loadImage("Wolf_Left.png"); // has the wolf facing left
        wolf_img.resize(120, 75);
        image(wolf_img, wolfX[i], layers[i]);
        wolfX[i] -= wolfSpeed[i]; // makes the wolf move
      }
    }
    if (wolfX[i] >= 1000 || wolfX[i] <= -200) {
      Direction[i] = !Direction[i]; // turn around
    }
  }
}
void game() {// adds score as the player moves forward
  score_text = "Score "+ score;
  fill(0);
  textSize(32);
  text(score_text, 100, 30); // print score text
  if (playerY < 0) { // if player hits the top reset it
    layer_num = 750;
    playerX = 400;
    playerY = 775;
    resetLayers();
  }
  // every 75 pixels add score
  if (playerY+50 <= layer_num) {
    layer_num -= 75;
    score += 1;
  }
}

void resetLayers() {// randomly generates the layers
  acornX = round(random(200, 600)); // randomly generates acorns
  acornY = round(random(200, 600));
  for (int i = 1; i < 10; i++) {
    layer_type = random(0, 5);
    Direction[i] = random(1) < 0.5;
    // random layers for water
    if (layer_type >= 3) {// set water variables
      layerColor[i] = blue;
      layerOutline[i] = darkblue;
      SpawnObstacle[i] = false;
      SpawnLog[i] = true;
      boatX[i] = random(-200, 1000);
      boatSpeed[i] = round(random(1, 3));
    }
    // random layers for road
    else if (layer_type >= greens && layer_type < 3) { // set road variables
      layerColor[i] = gray;
      layerOutline[i] = white;
      SpawnObstacle[i] = true;
      SpawnLog[i] = false;
      wolfX[i] = random(-200, 1000);
      wolfSpeed[i] = round(random(1, 3));
    }
    // random layers for grass
    else { // set grass variables
      layerColor[i] = green;
      layerOutline[i] = darkgreen;
      SpawnObstacle[i] = false;
      SpawnLog[i] = false;
    }
  }
  greens -= 0.2; // decreases the amount of green areas that spawn as the rounds go on
}

void end() {// the end screen
  game = "GAME OVER. Hit enter to restart"; // print ending messages
  score_text = "Total Score "+ (score*acorns);
  fill(red);
  textSize(50);
  text(game, 50, 350);
  text(score_text, 300, 440);
  player_speed = 0; // freeze player and obstacles
  for (int i = 0; i < 10; i++) {
    boatSpeed[i] = 0;
    wolfSpeed[i] = 0;
  }
}

void acorn() {// generates random power up for the acorns and loads the acorn
  acorn_img = loadImage("Acorn.png");
  acorn_img.resize(50, 50);
  image(acorn_img, acornX, acornY); // draw acorn
  if (playerX + 50 >= acornX && playerX <= acornX + 50 &&
    playerY + 50 >= acornY && playerY <= acornY + 50) { // if player hits acorn
    acornX = 1300; // hide acorn
    acorns +=1; // add to acorn
    power = round(random(1, 3));
    if (power == 1) { // give random power up
      player_speed = 30;
      powerup = "speed"; // increase speed
    }
    if (power == 2) {
      player_speed = 10;
      powerup = "slow"; // decrease speed
    }
    if (power == 3) {
      powerup = "fast obs";  // increase obstacle speed until player reaches top
      for (int i = 0; i < 10; i++) {// it will reset to normal after player reaches top
        boatSpeed[i] = 5;
        wolfSpeed[i] = 5;
      }
    }
  }
  // for start of game when there is no power up nothing will print
  if (powerup != null) {
    power_msg = ("Power " + powerup);
    text(power_msg, 350, 30);
  }
  acorn_msg = ("Acorns " + acorns); // how many acorns were collected
  fill(0);
  textSize(32);
  text(acorn_msg, 600, 30);
}
