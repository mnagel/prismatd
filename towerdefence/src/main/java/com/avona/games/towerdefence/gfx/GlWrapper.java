package com.avona.games.towerdefence.gfx;

import java.nio.Buffer;

public abstract class GlWrapper {
	public int GL_FLOAT;
	public int GL_UNSIGNED_SHORT;
	public int GL_TRIANGLES;
	public int GL_TEXTURE0;
	public int GL_TEXTURE_2D;

	public abstract void glVertexAttribPointer(int indx, int size, int type, boolean normalized, int stride, Buffer ptr);

	public abstract void glEnableVertexAttribArray(int index);

	public abstract void glDrawElements(int mode, int count, int type, Buffer indices);

	public abstract void glDrawArrays(int mode, int first, int count);

	public abstract void glDisableVertexAttribArray(int index);

	public abstract void glUniformMatrix4fv(int location, int count, boolean transpose, float[] value, int value_offset);

	public abstract void glUseProgram(int program);

	public abstract void glActiveTexture(int texture);

	public abstract void glBindTexture(int target, int texture);

	public abstract void glUniform1i(int location, int v0);

	public abstract void glUniform4fv(int location, int count, float[] value, int value_offset);
}
