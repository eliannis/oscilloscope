# Oscilloscope
A sound-card oscilloscope app for Android devices.

![Trace](/img/trace.png)
![Trace Settings](/img/trace-settings.png)

## Theory of Operation
Sound-cards in mobile devices and computers use a digital-to-analog converter (ADC) to convert 
microphone input voltages to digital data. In the Android API, the AudioRecorder class can be used 
to read ADC output as 16-bit pulse-coded modulated (PCM) data. The oscilloscope application displays 
this data as a graphical trace.

### Limitations
The ADC in most mobile devices is designed for audio frequencies. The exact range will be different
for individual device, but most should be able to sample frequencies between 300Hz and  20kHz. DC
level voltages should be filtered out in well designed sound-cards.

Voltage levels at the microphone input are expected to be quite low. The 
[Android 3.5mm Headset Jack Specification](https://source.android.com/devices/accessories/headset/jack-headset-spec) 
requires a bias voltage of 1.8V - 2.9V. Therefore, it is reasonable to expect the max input peak-to-peak 
voltage should be no more than 3V - 5V. There are external circuits that can be constructed, 
to ensure input voltages fall within this range.

# Features

## Current Features
* Pause/Resume sampling
* Vertical trace scaling

## Future Features
* Horizontal trace scaling
* Trigger Settings
* Improved trace stability