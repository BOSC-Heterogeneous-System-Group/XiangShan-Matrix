package xiangshan.backend.fu.mpu

import chipsalliance.rocketchip.config.Parameters
import chisel3._
import chisel3.util._
import xiangshan._

class MatrixReg(implicit p: Parameters) extends XSModule {
  val io = IO(new Bundle() {
    val storeDataFromStd = Vec(StorePipelineWidth, Flipped(Valid(new ExuOutput))) // from stdExeUnits
    val storeDataToLsq = Vec(StorePipelineWidth,Valid(new ExuOutput)) // to lsq
  })

  //val dataValid = Bool()

  for (i <- 0 until exuParameters.StuCnt) {
    when (io.storeDataFromStd(i).bits.uop.ctrl.mpu.isMatrixStore) {
      io.storeDataToLsq(i).bits.uop := io.storeDataFromStd(i).bits.uop
      io.storeDataToLsq(i).bits.fflags := io.storeDataFromStd(i).bits.fflags
      io.storeDataToLsq(i).bits.redirectValid := io.storeDataFromStd(i).bits.redirectValid
      io.storeDataToLsq(i).bits.redirect := io.storeDataFromStd(i).bits.redirect
      io.storeDataToLsq(i).bits.debug := io.storeDataFromStd(i).bits.debug

      io.storeDataToLsq(i).bits.data := 1234.U(XLEN.W)
      io.storeDataToLsq(i).valid := io.storeDataFromStd(i).valid
    }.otherwise{
      io.storeDataToLsq(i) := io.storeDataFromStd(i)
    }
  }




}