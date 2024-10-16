//package bench
//
//import com.google.common.hash.{Hasher, Hashing}
//import org.apache.commons.codec.digest.Blake3
//import org.bouncycastle.jce.provider.BouncyCastleProvider
//import org.openjdk.jmh.annotations._
//import org.openjdk.jmh.infra.Blackhole
//
//import java.security.{MessageDigest, Security}
//import java.util.concurrent.TimeUnit
//import scala.util.Random
//
//@BenchmarkMode(Array(Mode.AverageTime))
//@OutputTimeUnit(TimeUnit.MILLISECONDS)
//@Warmup(iterations = 2, time = 5, timeUnit = TimeUnit.SECONDS)
//@Measurement(iterations = 3, time = 3, timeUnit = TimeUnit.SECONDS)
//@State(Scope.Benchmark)
//class Millis {
//
//  @Param(Array[Int]("1000000", "10000000"))
//  var payloadSize: Int = 0
//  var payload: Array[Byte] = _
//
//  var jdkHasher: MessageDigest = _
//  var blake3Hasher: Blake3 = _
//  var bcBlake3: MessageDigest = _
//
//  @Setup
//  def setup(): Unit = {
//    Security.addProvider(new BouncyCastleProvider)
//    payload = Random.nextBytes(payloadSize)
//    jdkHasher = MessageDigest.getInstance("SHA-256")
//    blake3Hasher = Blake3.initHash()
//    bcBlake3 = MessageDigest.getInstance("BLAKE3-256")
//  }
//
//  @Benchmark
//  def bouncyCastleBlake3(): Array[Byte] = {
//    bcBlake3.update(payload)
//    bcBlake3.digest()
//  }
//
//  @Benchmark
//  def defaultJdkSHA256(): Array[Byte] = {
//    jdkHasher.update(payload)
//    jdkHasher.digest()
//  }
//
//  @Benchmark
//  def commonsBlake3(): Array[Byte] = {
//    blake3Hasher.update(payload)
//    blake3Hasher.doFinalize(32)
//  }
//
////  @Benchmark
////  def guavaSHA256(): Array[Byte] = {
////    val guavaHasher = Hashing.sha256().newHasher()
////
////    guavaHasher.putBytes(payload)
////    guavaHasher.hash().asBytes()
////  }
//}
