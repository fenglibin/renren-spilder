package it.renren.spilder.dataobject;

import java.io.Serializable;

public class ArctypeDO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8435597933201618777L;
	int                       id;
	String                    typename;
    public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTypename() {
		return typename;
	}
	public void setTypename(String typename) {
		this.typename = typename;
	}	
}
