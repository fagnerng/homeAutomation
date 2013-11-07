package br.edu.ufcg.ccc.homeautomation.networking;

import java.io.File;

public interface OnDownloadProgressListener {
	
	public void onProgress(int progress, File file);

}
