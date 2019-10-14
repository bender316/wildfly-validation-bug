package boundary.model;

import java.io.Serializable;

import javax.validation.constraints.Size;

public class MyModel implements Serializable {

	@Size(max = 3)
	String value;

	public String getValue() {
		return value;
	}

	public void setValue(final String value) {
		this.value = value;
	}

}
