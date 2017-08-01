package indi.ninet.amplifierprove.service;

import indi.ninet.amplifierprove.domain.AmplifierInf;

/**
 * 
 * @author wfzhou 2017/05/01 放大器定位的顶层接口
 *
 */
public interface ActiveProve {
	public void prove();
	public AmplifierInf getAmplifierInf();
}
