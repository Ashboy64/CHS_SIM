using UnityEngine;
using System.Collections;
using System.Collections.Generic;

public class Controller1 : MonoBehaviour {
    public List<AxleInfo> axleInfos; // the information about each individual axle
    public float maxMotorTorque; // maximum torque the motor can apply to wheel
    public float maxSteeringAngle; // maximum steer angle the wheel can have
    public float torqueMultiplier; // Multiplier for torque
    public float rpmMultiplier; // Multiplier for torque

    public void FixedUpdate()
    {
        float motor = maxMotorTorque * Input.GetAxis("Vertical");
        float steering = maxSteeringAngle * Input.GetAxis("Horizontal");

        foreach (AxleInfo axleInfo in axleInfos) {
            if (axleInfo.motor) {
                float targetleftWheelRPM = 1; // fill in with code
                if (axleInfo.leftWheel.rpm <= rpmMultiplier*targetleftWheelRPM){
                    axleInfo.leftWheel.motorTorque = maxMotorTorque;
                } else {
                    axleInfo.leftWheel.motorTorque = 0;
                }
                float targetrightWheelRPM = 1; // fill in with code
                if (axleInfo.rightWheel.rpm <= rpmMultiplier*targetrightWheelRPM){
                    axleInfo.rightWheel.motorTorque = maxMotorTorque;
                } else {
                    axleInfo.rightWheel.motorTorque = 0;
                }
            }
        }
    }
}

[System.Serializable]
public class AxleInfo {
    public WheelCollider leftWheel;
    public WheelCollider rightWheel;
    public bool motor; // is this wheel attached to motor?
    public bool steering; // does this wheel apply steer angle?
}