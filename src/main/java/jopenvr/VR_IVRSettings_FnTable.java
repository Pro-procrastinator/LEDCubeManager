package jopenvr;
import com.sun.jna.Callback;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import java.util.Arrays;
import java.util.List;
/**
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class VR_IVRSettings_FnTable extends Structure {
	/** C type : GetSettingsErrorNameFromEnum_callback* */
	public VR_IVRSettings_FnTable.GetSettingsErrorNameFromEnum_callback GetSettingsErrorNameFromEnum;
	/** C type : Sync_callback* */
	public VR_IVRSettings_FnTable.Sync_callback Sync;
	/** C type : GetBool_callback* */
	public VR_IVRSettings_FnTable.GetBool_callback GetBool;
	/** C type : SetBool_callback* */
	public VR_IVRSettings_FnTable.SetBool_callback SetBool;
	/** C type : GetInt32_callback* */
	public VR_IVRSettings_FnTable.GetInt32_callback GetInt32;
	/** C type : SetInt32_callback* */
	public VR_IVRSettings_FnTable.SetInt32_callback SetInt32;
	/** C type : GetFloat_callback* */
	public VR_IVRSettings_FnTable.GetFloat_callback GetFloat;
	/** C type : SetFloat_callback* */
	public VR_IVRSettings_FnTable.SetFloat_callback SetFloat;
	/** C type : GetString_callback* */
	public VR_IVRSettings_FnTable.GetString_callback GetString;
	/** C type : SetString_callback* */
	public VR_IVRSettings_FnTable.SetString_callback SetString;
	/** C type : RemoveSection_callback* */
	public VR_IVRSettings_FnTable.RemoveSection_callback RemoveSection;
	/** C type : RemoveKeyInSection_callback* */
	public VR_IVRSettings_FnTable.RemoveKeyInSection_callback RemoveKeyInSection;
	public interface GetSettingsErrorNameFromEnum_callback extends Callback {
		Pointer apply(int eError);
	};
	public interface Sync_callback extends Callback {
		byte apply(byte bForce, IntByReference peError);
	};
	public interface GetBool_callback extends Callback {
		byte apply(Pointer pchSection, Pointer pchSettingsKey, byte bDefaultValue, IntByReference peError);
	};
	public interface SetBool_callback extends Callback {
		void apply(Pointer pchSection, Pointer pchSettingsKey, byte bValue, IntByReference peError);
	};
	public interface GetInt32_callback extends Callback {
		int apply(Pointer pchSection, Pointer pchSettingsKey, int nDefaultValue, IntByReference peError);
	};
	public interface SetInt32_callback extends Callback {
		void apply(Pointer pchSection, Pointer pchSettingsKey, int nValue, IntByReference peError);
	};
	public interface GetFloat_callback extends Callback {
		float apply(Pointer pchSection, Pointer pchSettingsKey, float flDefaultValue, IntByReference peError);
	};
	public interface SetFloat_callback extends Callback {
		void apply(Pointer pchSection, Pointer pchSettingsKey, float flValue, IntByReference peError);
	};
	public interface GetString_callback extends Callback {
		void apply(Pointer pchSection, Pointer pchSettingsKey, Pointer pchValue, int unValueLen, Pointer pchDefaultValue, IntByReference peError);
	};
	public interface SetString_callback extends Callback {
		void apply(Pointer pchSection, Pointer pchSettingsKey, Pointer pchValue, IntByReference peError);
	};
	public interface RemoveSection_callback extends Callback {
		void apply(Pointer pchSection, IntByReference peError);
	};
	public interface RemoveKeyInSection_callback extends Callback {
		void apply(Pointer pchSection, Pointer pchSettingsKey, IntByReference peError);
	};
	public VR_IVRSettings_FnTable() {
		super();
	}
	protected List<? > getFieldOrder() {
		return Arrays.asList("GetSettingsErrorNameFromEnum", "Sync", "GetBool", "SetBool", "GetInt32", "SetInt32", "GetFloat", "SetFloat", "GetString", "SetString", "RemoveSection", "RemoveKeyInSection");
	}
	public VR_IVRSettings_FnTable(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends VR_IVRSettings_FnTable implements Structure.ByReference {
		
	};
	public static class ByValue extends VR_IVRSettings_FnTable implements Structure.ByValue {
		
	};
}
