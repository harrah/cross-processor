import sbt.{processor, Project}
import processor.{Processor, ProcessorResult, Success}
import Function.tupled

class CrossProcessor extends Processor
{
	/** Similar to build.scala.versions, this is the name of the property that specifies the space-delimited versions of Lift to cross-build.*/
	val VersionsProp = "build.lift.versions"

	/** The name of the property that defines the version of lift actually used for the current build.*/
	val CurrentVersionProp = "current.lift.version"

	/** Invokes this processor.  `args` contains the command to run for all combinations of lift and scala versions.*/
   def apply(label: String, project: Project, onFailure: Option[String], args: String): ProcessorResult =
	{
		// get the requested lift versions ...
		val liftVersions = project.propertyNamed(VersionsProp).value.toString.split("""\s+""")
		//  ... and Scala versions
		val scalaVersions = project.crossScalaVersions

		// build up all combinations
		val combinations = for(lv <- liftVersions; sv <- scalaVersions) yield (lv, sv)

		// translate the combinations into commands
		val commands = combinations flatMap tupled(setVersions(args))

		new Success(project, onFailure, commands :_*)
	}

	/** Command that sets the current lift version to use.*/
	def setLiftVersion(v: String) =
		"set " + CurrentVersionProp + " " + v

	/** Command that sets the current Scala version to use. */
	def setScalaVersion(v: String) =
		"++" + v

	/** Commands that set the lift and scala versions to use.*/
	def setVersions(command: String)(liftV: String, scalaV: String) =
		setScalaVersion(scalaV) :: setLiftVersion(liftV) :: "reload" :: command :: Nil
}
