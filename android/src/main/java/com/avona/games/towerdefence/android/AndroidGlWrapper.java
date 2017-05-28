package com.avona.games.towerdefence.android;

import android.opengl.GLES20;
import com.avona.games.towerdefence.gfx.GlWrapper;

import java.nio.Buffer;

public class AndroidGlWrapper extends GlWrapper {
	public AndroidGlWrapper() {
		GL_FLOAT = GLES20.GL_FLOAT;
		GL_UNSIGNED_SHORT = GLES20.GL_UNSIGNED_SHORT;
		GL_TRIANGLES = GLES20.GL_TRIANGLES;
		GL_TEXTURE0 = GLES20.GL_TEXTURE0;
		GL_TEXTURE_2D = GLES20.GL_TEXTURE_2D;
	}

	@Override
	public void glVertexAttribPointer(int indx, int size, int type, boolean normalized, int stride, Buffer ptr) {
		GLES20.glVertexAttribPointer(indx, size, type, normalized, stride, ptr);
	}

	@Override
	public void glEnableVertexAttribArray(int index) {
		GLES20.glEnableVertexAttribArray(index);
	}

	@Override
	public void glDrawElements(int mode, int count, int type, Buffer indices) {
		GLES20.glDrawElements(mode, count, type, indices);
	}

	@Override
	public void glDrawArrays(int mode, int first, int count) {
		GLES20.glDrawArrays(mode, first, count);
	}

	@Override
	public void glDisableVertexAttribArray(int index) {
		GLES20.glDisableVertexAttribArray(index);
	}

	@Override
	public void glUniformMatrix4fv(int location, int count, boolean transpose, float[] value, int value_offset) {
		GLES20.glUniformMatrix4fv(location, count, transpose, value, value_offset);
	}

	@Override
	public void glUseProgram(int program) {
		GLES20.glUseProgram(program);
	}

	@Override
	public void glActiveTexture(int texture) {
		GLES20.glActiveTexture(texture);
	}

	@Override
	public void glBindTexture(int target, int texture) {
		GLES20.glBindTexture(target, texture);
	}

	@Override
	public void glUniform1i(int location, int v0) {
		GLES20.glUniform1i(location, v0);
	}

	@Override
	public void glUniform4fv(int location, int count, float[] value, int value_offset) {
		GLES20.glUniform4fv(location, count, value, value_offset);
	}
}
