import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Cat_Game extends PApplet {

//By Trevor Thacker
ball baal;
int r;
int g;
int b = 255;
int phase;
float timeSince = 0;//Timers are in milliseconds
float mouseTimer = 10000;

ArrayList<block> copy = new ArrayList<block>();
ArrayList<block> boxes = new ArrayList<block>();

public void setup(){
  baal = new ball();
  //size(700,500);
  
  stroke(255);
  strokeWeight(height/600);
  textSize(15);
}

public void draw(){
  
  if(baal.distLeft <= 0){
    boxes.add(new block(baal.x,baal.y,1000));
    baal.distLeft = random(50,300);
  }
  clear();
  if(mouseButton==RIGHT){
    copy = new ArrayList<block>(boxes);
    for(block temp:copy){
      if(abs((mouseX-temp.getX())*(mouseX-temp.getX()))+((mouseY-temp.getY())*(mouseY-temp.getY())) <= temp.radius*temp.radius)
        boxes.remove(temp);
    }
  }
  
  fill(r,g,b);
  baal.drawBall();
  
  for(block temp:boxes){//Draw all blocks
    if(temp.load > 0)temp.load -= millis() - timeSince;
    fill(r,g,b,temp.alp());//Sets a block to transparent if it doesnt have collision
    ellipse(temp.getX(),temp.getY(),temp.radius*2,temp.radius*2);
  }
  if(mouseTimer > 0){
    fill(255,255,255,map(mouseTimer,0,10000,0,255));
    text("LMB: Place square. RMB: Remove square. Scroll to change ball speed. Time Passed: "+second()+"s",5,15);
  }
  mouseTimer -= millis() - timeSince;
  baal.distLeft -= sqrt((baal.xMove*baal.xMove)+(baal.yMove*baal.yMove)); 
  timeSince = millis();
  rainbow();
}

public void mousePressed(){
  if(mouseButton==LEFT){
    boxes.add(new block(mouseX,mouseY,0));
  }
}
public void mouseMoved(){
  mouseTimer = 10000;
}
public void mouseWheel(MouseEvent e){
  if(baal.speedMulti > 0){
    if(e.getCount() < 0){
      baal.speedMulti *= 1.2f;
      baal.xMove *= 1.2f;
      baal.yMove *= 1.2f;
    }
    else{
      baal.speedMulti /= 1.2f;
      baal.xMove /= 1.2f;
      baal.yMove /= 1.2f;
    }
  }
}
public void checkCollision(){
  block destroy = null;
  float x = baal.x;
  float y = baal.y;
  for(block temp:boxes){//Check for collision will any blocks
    if(temp.hasCollision()){
      if(abs((x-temp.getX())*(x-temp.getX()))+((y-temp.getY())*(y-temp.getY())) <= (baal.radius+temp.radius)*(baal.radius+temp.radius) && x-temp.getX() != 0 && y-temp.getY() != 0){
        baal.xMove=baal.speedMulti*map(x-temp.getX(),0,sqrt(((x-temp.getX())*(x-temp.getX()))+((y-temp.getY())*(y-temp.getY()))),0,1);
        baal.yMove=baal.speedMulti*map(y-temp.getY(),0,sqrt(((y-temp.getY())*(y-temp.getY()))+((y-temp.getY())*(y-temp.getY()))),0,1);
        temp.hp--;
        if(temp.hp <= 0)destroy = temp;//Will mark a block to be destroyed if its hp is 0
      }
    }
  }
  if(destroy != null)boxes.remove(destroy);
  //Check for collision with screen borders
  if(y-15<=0)baal.yMove=-baal.yMove;
  if(y+15>=height)baal.yMove=-baal.yMove;
  if(x-15<=0)baal.xMove=-baal.xMove;
  if(x+15>=width)baal.xMove=-baal.xMove;
}

public void rainbow(){
  if(phase==0){
    r++; 
    b--; 
  }
  if(phase==1){
    g++; 
    r--; 
  }
  if(phase==2){
    b++; 
    g--; 
  }
  if(r==255||g==255||b==255){
    if(phase==2)phase=-1;
    phase++;
  }
}

public class ball{
  float radius;
  float x;//Location of the ball
  float y;
  float xMove;//X movement distance per frame
  float yMove;//Y movement distance per frame
  float speedMulti;//Speed multiplier for the circle
  float distLeft = 100;
  
  ball(){
    radius = height/35;
    x = width/2;
    y = height/2;
    xMove = width/200;
    yMove = height/200;
    speedMulti = height/200;
  }
  
  ball(float d){
    radius = d;
    x = width/2;
    y = height/2;
    xMove = width/50;
    yMove = height/50;
    speedMulti = height/50;
  }
  
  public void drawBall(){
    checkCollision();
    ellipse(x,y,radius*2,radius*2);
    x+=xMove;
    y+=yMove;
  }
}
public class block{
  float radius = height/140;
  float x;
  float y;
  int hp = 2;
  int load;
  
  block(float xi, float yi){
    x = xi; 
    y = yi;
    load = 1000;
  }
  
  block(float xi, float yi, int t){
    x = xi; 
    y = yi;
    load = t;
  }
  
  public float getX(){return x;}
  public float getY(){return y;}
  
  public boolean hasCollision(){
    if(load <= 0)return true;
    return false;
  }
  
  public int alp(){
    if(hasCollision())return 255;
    return 0;
  }
}
  public void settings() {  fullScreen(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Cat_Game" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
