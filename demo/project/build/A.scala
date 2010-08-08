import sbt._

class A(info: ProjectInfo) extends DefaultProject(info) with CrossLift

trait CrossLift extends Project
{
	lazy val buildLiftVersions = property[String]
	def crossLiftVersions = buildLiftVersions.value.split("""\s+""").toSeq

	lazy val currentLiftVersion = propertyOptional[String](crossLiftVersions.first)

	override def crossPath(base: Path) = base / ("scala_" + buildScalaVersion) / ("lift_" + currentLiftVersion.value)
	override def crossScalaVersionString = super.crossScalaVersionString + "_" + currentLiftVersion.value
}
