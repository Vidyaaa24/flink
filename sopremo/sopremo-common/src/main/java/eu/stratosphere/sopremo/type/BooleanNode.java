package eu.stratosphere.sopremo.type;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Michael Hopstock
 * @author Tommy Neubert
 */
public class BooleanNode extends JsonNode implements IPrimitiveNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9185727528566635632L;

	public final static BooleanNode TRUE = new BooleanNode(true);

	public final static BooleanNode FALSE = new BooleanNode(false);

	private boolean value;

	/**
	 * Initializes a BooleanNode which represents <code>false</code>. This constructor is needed for serialization and
	 * deserialization of BooleanNodes, please use BooleanNode.valueOf(boolean) to get an instance of BooleanNode.
	 */
	public BooleanNode() {
		this(false);
	}

	private BooleanNode(final boolean v) {
		this.value = v;
	}

	@Override
	public Boolean getJavaValue() {
		return this.value;
	}

	/**
	 * Returns the instance of BooleanNode which represents the given <code>boolean</code>.
	 * 
	 * @param b
	 *        the value for which the BooleanNode should be returned for
	 * @return the BooleanNode which represents the given value
	 */
	public static BooleanNode valueOf(final boolean b) {
		return b ? TRUE : FALSE;
	}

	/**
	 * Returns either this BooleanNode represents the value <code>true</code> or not.
	 */
	public boolean getBooleanValue() {
		return this == TRUE;
	}

	@Override
	public IJsonNode clone() {
		return this;
	}

	@Override
	public StringBuilder toString(final StringBuilder sb) {
		return this == TRUE ? sb.append("true") : sb.append("false");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (this.value ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;

		final BooleanNode other = (BooleanNode) obj;
		if (this.value != other.value)
			return false;
		return true;
	}

	@Override
	public BooleanNode canonicalize() {
		return this.value ? TRUE : FALSE;
	}

	@Override
	public void read(final DataInput in) throws IOException {
		this.value = in.readInt() == 1;
	}

	private Object readResolve() {
		return valueOf(this.value);
	}

	@Override
	public void write(final DataOutput out) throws IOException {
		out.writeInt(this.value ? 1 : 0);
	}

	@Override
	public Type getType() {
		return Type.BooleanNode;
	}

	@Override
	public int compareToSameType(final IJsonNode other) {
		return (this.value ? 1 : 0) - (((BooleanNode) other).value ? 1 : 0);
	}

	@Override
	public void clear() {
	}

	@Override
	public int getMaxNormalizedKeyLen() {
		return 1;
	}

	@Override
	public void copyNormalizedKey(byte[] target, int offset, int len) {

		if (len >= this.getMaxNormalizedKeyLen()) {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			try {
				this.write(new DataOutputStream(stream));
				byte[] result = stream.toByteArray();
				target[offset] = result[result.length - 1];
				for (int i = 1; i < len; i++) {
					target[offset + i] = (byte) 0;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
