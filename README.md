# ProcessingParameters
A library that makes it easy to make linear parameters that are controlled by mouse, midi, keyboard, or OSC input.

This project is for visual artists and musicians who use Processing (processing.org) (or any any other java framework) and want to easily add parameters to their designs which can be easily controlled by one of several sources.  The user of this library needs simply to:

- Create a new ParameterManager
- Use the parameter manager to create new Parameters.

Each Parameter is a value from 0 to 1.  A UI is automatically built which displays a slider showing the value of the parameter. If you edit the settings of a Parameter you can have it listen for midi CC messages, mouse X or Y movements, and hopefully soon, OSC messages.

There are two Java projects in this repo, the Parameters library, and a test sandbox which demonstrates the basic creation and usage of a Parameter.

[Parameters](https://github.com/hieberr/ProcessingParameters/tree/master/Parameters)

[ParametersTestSandBox](https://github.com/hieberr/ProcessingParameters/tree/master/ParametersTestSandBox)

