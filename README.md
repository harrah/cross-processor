A demo processor for cross-building against other properties besides Scala version.

The root directory contains the processor.  To build it, run
	sbt update publish-local

This will make the processor available on your local machine.  The 'CrossProcessor.scala' file contains the processor definition and is commented to explain what it does.

The 'demo' directory contains a sample project.  It has a project definition and one dummy source file as an example.

The project definition does two things.  First, it defines the properties expected by the processor: 'build.lift.versions' and 'current.lift.version'.  A user should populate 'build.lift.versions' with the space-separated versions to build against.  This is already done in the sample project.  The 'current.lift.version' property defines the current version of lift being built.

The second thing done in the project definition is overriding the output locations and the module ID to include the lift version.  The output directories ('target' and 'lib_managed') get an extra subpath with the lift version and the module ID gets an extra underscore followed by the lift version.

To use, declare the processor (only needs to be done once per machine):
	> *cross is org.scala-tools.sbt cross 1.0

Then, build something:
	> cross compile
or
	> cross publish-local

You can select different dependencies for different lift versions by testing 'currentLiftVersion.value'.