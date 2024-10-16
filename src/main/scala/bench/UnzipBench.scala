package bench

import org.apache.commons.io.FileUtils
import org.openjdk.jmh.annotations._
import sbt.io.IO

import java.io.{File, FileOutputStream}
import java.util.concurrent.TimeUnit
import java.util.zip.ZipFile
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}

@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 2, time = 2, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 2, time = 3, timeUnit = TimeUnit.SECONDS)
@State(Scope.Benchmark)
class UnzipBench {
  private val files = List(
    "/Users/idragos/.sbt/1.0/.develocity/build-cache/e1fdb7c27565222f8a56cab6dce87ec5 2/tree-compilation-products",
    "/Users/idragos/.sbt/1.0/.develocity/build-cache/e41ea87e8945209aa2f254f214f056cf 2/tree-compilation-products",
    "/Users/idragos/.sbt/1.0/.develocity/build-cache/3f8f34cbb7b59d0aaaeae6a61eeb3fbc 2/tree-compilation-products"
  )

  @Param(Array[Int]("1", "5", "10"))
  var threads: Int = 0

  @Setup
  def setup(): Unit = {
    FileUtils.deleteDirectory(new File("/tmp/bench"))
  }

  @Benchmark
  def sbtIoUnzip(): Unit = {
    val futures = for (i <- 0 until threads) yield {
      val destDir = new File(s"/tmp/bench/sbt-io-unzip${i}")
      Future { files.foreach(f => IO.unzip(new File(f), destDir)) }
    }

    futures.foreach(Await.result(_, scala.concurrent.duration.Duration.Inf))
  }

  @Benchmark
  def commonsUnzip(): Unit = {
    val futures = for (i <- 0 until threads) yield {
      val destDir = new File(s"/tmp/bench/commons-unzip${i}")
      Future {
        destDir.mkdirs()
        files.foreach { file =>
          try {
            val zipFile = new ZipFile(file)
            try {
              val entries = zipFile.entries
              while (entries.hasMoreElements) {
                val entry = entries.nextElement
                val entryDestination = new File(destDir, entry.getName)
                if (entry.isDirectory) entryDestination.mkdirs
                else {
                  entryDestination.getParentFile.mkdirs
                  try {
                    val out = new FileOutputStream(entryDestination)
                    try zipFile.getInputStream(entry).transferTo(out)
                    finally {
                      if (out != null) out.close()
                    }
                  }
                }
              }
            } finally if (zipFile != null) zipFile.close()
          }
        }
      }
    }
    futures.foreach(Await.result(_, scala.concurrent.duration.Duration.Inf))
  }
}
