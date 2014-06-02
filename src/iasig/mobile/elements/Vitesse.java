package iasig.mobile.elements;

public interface Vitesse {

public double[] vitessekm = {30,50,90,110,130}; //Vitesse en km.h-1
public double[] vitesse = {30/3.6,50/3.6,90/3.6,110/3.6,130/3.6}; //Vitesse en m.s-1
public double[] acceleration = {4.16,2.7,2.7,2,1.85}; //Acceleration en m.s-2
public double[] tempsacceleration = {vitesse[0]/acceleration[0],
								vitesse[0]/acceleration[0] + (vitesse[1]-vitesse[0])/acceleration[1],
								vitesse[0]/acceleration[0] + (vitesse[1]-vitesse[0])/acceleration[1] + (vitesse[2]-vitesse[1])/acceleration[2],
								vitesse[0]/acceleration[0] + (vitesse[1]-vitesse[0])/acceleration[1] + (vitesse[2]-vitesse[1])/acceleration[2] + (vitesse[3]-vitesse[2])/acceleration[3],
								vitesse[0]/acceleration[0] + (vitesse[1]-vitesse[0])/acceleration[1] + (vitesse[2]-vitesse[1])/acceleration[2] + (vitesse[3]-vitesse[2])/acceleration[3] +(vitesse[4]-vitesse[3])/acceleration[4]};


public double distance0 = acceleration[0]* tempsacceleration[0]*tempsacceleration[0]/2;
public double distance1 = distance0 + vitesse[0]* tempsacceleration[1] +acceleration[1]* tempsacceleration[1]*tempsacceleration[1]/2;
public double distance2 = distance1 + vitesse[1]* tempsacceleration[2] +acceleration[2]* tempsacceleration[2]*tempsacceleration[2]/2;
public double distance3 = distance2 + vitesse[2]* tempsacceleration[3] +acceleration[3]* tempsacceleration[3]*tempsacceleration[3]/2;
public double distance4 = distance3 + vitesse[3]* tempsacceleration[4] +acceleration[4]* tempsacceleration[4]*tempsacceleration[4]/2;
public double[] distance = {distance0,distance1,distance2,distance3,distance4}; // distance parcourue pour atteindre chacune des vitesses

}
