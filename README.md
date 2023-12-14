# Project_ten

This Android app is developed using Jetpack Compose to showcase the gesture and sensor APIs. It consists of two activities: Sensor Activity and Gesture Activity, each demonstrating specific functionalities related to sensors and gestures.

## Sensor Activity

The Sensor Activity provides information about the user, location, temperature, and a selected sensor value. It uses the Geocoder class to fetch the user's location and displays it along with the current temperature. Additionally, a chosen sensor value, such as Air Pressure, is showcased.

### Features:

- **User Information**: Displays the user's name, state, and city.
- **Temperature**: Shows the current temperature.
- **Sensor Value**: Displays the value of a selected sensor (excluding Accelerometer and Light sensor).
- **Gesture Playground Button**: To navigate to the Gesture Activity, use the "Gesture Playground" button, which supports fling operations.

### Note:

To obtain the location, the app uses the `getFromLocation()` method from the Geocoder class. It iterates through the list of addresses to get the first address, setting `maxResults` to 1.

## Gesture Activity

The Gesture Activity is divided into two fragments:

### Top Fragment:

- Displays a red ball that moves based on user gestures.
- The ball responds to swipe gestures, moving in the direction of the swipe.

### Bottom Fragment:

- Logs activities performed in the top fragment.
- Records the name of each gesture performed, updating in real-time.

### Note:

The `onTouch()` method from the Android API is used to capture one-finger swipe gestures in the top fragment.

## Bonus Feature (10%)

A third activity replicates the Gesture Activity but uses a sensor, like the accelerometer, to move the ball within the fragment. The list of gestures is no longer included in this activity.

## Video Walkthrough

## Video Walkthrough
https://www.loom.com/share/7c27ac4cdbc0435d9cc9b6cebdf396ef


## Notes

The app's logic is fully implemented and functional. However, there might be a minor issue causing it not to compile.

## License

Copyright 2023 by [George Sackie]

Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with the License. You may obtain a copy of the License at [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0).

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
