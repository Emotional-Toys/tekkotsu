info.FrameTime=8;
info.NumFrames=4;
info.SlowFrameTime=128;
info.NumSlowFrames=1;
info.SoundBufferTime=32;
info.NumPIDJoints=15
info.NumBinJoints=0;
info.NumLEDs=20;
info.NumOutputs=info.NumPIDJoints + info.NumBinJoints + info.NumLEDs;
info.isFastOutput=zeros(info.NumOutputs)+1; %all ok!
info.isRealERS220=info.isFastOutput %umm they're the same
info.JointsPerLeg=3;
info.NumLegs=4;
info.NumLegJoints=info.JointsPerLeg*info.NumLegs;
info.NumTailJoints=0;
info.NumMouthJoints=0;
info.NumEarJoints=0;
info.NumButtons=11;
info.NumSensors=1+3+1+5;
% offset data
info.LogOffset.positions=2;
info.LogOffset.duties=info.LogOffset.positions+info.NumPIDJoints;
info.LogOffset.sensors=info.LogOffset.duties+info.NumPIDJoints;
info.LogOffset.buttons=info.LogOffset.sensors+6;
info.SensorOffsetOf.IRDist=0;
info.SensorOffsetOf.BAccelOffset=1;
info.SensorOffsetOf.LAccelOffset=2;
info.SensorOffsetOf.DAccelOffset=3;
