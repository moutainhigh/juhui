package cc.messcat.gjfeng.web.app.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cc.messcat.gjfeng.common.vo.app.ResultVo;
import cc.messcat.gjfeng.common.web.BaseController;
import cc.messcat.gjfeng.dubbo.core.GjfAppUpdateInfoDubbo;

@Controller
@RequestMapping(value="app/upgrede",headers = "app-version=v1.0")
public class AppUpgredeInfoControllerV1 extends BaseController{
	
	@Autowired
	@Qualifier("appUpdateInfoDubbo")
	private GjfAppUpdateInfoDubbo appUpdateInfoDubbo;
	
	/**
	 * 获取最新app版本信息
	 * @param type
	 * @return
	 */
	@RequestMapping(value="/v1_0/findAppUpgredeByType",method=RequestMethod.POST)
	@ResponseBody
	public Object findAppUpgredeByType(String type){
		try{
			resultVo=appUpdateInfoDubbo.findAppUpGradeByType(type);
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultVo;
	}
	
	/**
	 * 安卓推送
	 * @param deviceToken
	 * @return
	 */
	@RequestMapping(value="/v1_0/sendAndroidUnicast",method=RequestMethod.POST)
	@ResponseBody
	public Object sendAndroidUnicast(String deviceToken){
		try{
			//YoumemgPushUtils.sendAndroidUnicast(deviceToken);
		}catch(Exception e){
			e.printStackTrace();
			return new ResultVo(400, "推送失败");
		}
		return new ResultVo(200, "推送成功");
	}
	
	
}
