package com.horadadiversao.telas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import com.horadadiversao.R;
import com.horadadiversao.dados.VisualizacaoBD;
import com.horadadiversao.domain.PacoteDeTela;
import com.horadadiversao.domain.PacoteDeVideo;
import com.horadadiversao.domain.Video;
import com.horadadiversao.util.BackgroundSoundService;
import com.horadadiversao.util.CustomGridViewAdapter;
import com.horadadiversao.util.Item;
import com.horadadiversao.util.MusicActivityControl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * 
 * @author CASSIO SOUZA
 *
 */

public class MainActivity extends Activity {
	GridView gridView;
	ArrayList<Item> gridArray = new ArrayList<Item>();
	CustomGridViewAdapter customGridAdapter;

	private Handler backgroundHandler = new Handler();
	public static final String PREFS_NAME = "settings";
	private PacoteDeTela dadosTela = new PacoteDeTela();

	CallbackManager callbackManager;
	ShareDialog shareDialog;

	Intent serviceMusic;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//inicializando componente do facebook
		FacebookSdk.sdkInitialize(getApplicationContext());
		callbackManager = CallbackManager.Factory.create();
		shareDialog = new ShareDialog(this);

		this.registreRetornoFacebook();

		this.salvarOpcaoBotaoStartStopMusic(true);

		if(serviceMusic == null){
			serviceMusic = new Intent(this, BackgroundSoundService.class);
			startService(serviceMusic);
			Log.i("Musica: ", "Iniciou servico! ");
		}


		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_main);

		backgroundHandler.post(handle); //set background

		Bitmap homeIcon = null;
		Intent intent = getIntent();
		Bundle params = intent.getExtras();

		if(params!=null)
		{
			dadosTela = (PacoteDeTela) params.getSerializable("dadosTela");
		}

		for(PacoteDeVideo pacote : dadosTela.getPacoteDeVideo()){

			switch (pacote.getNomeDoPacote()) {

				case "bobzoom":
					homeIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.bobzoom);
					break;

				case "animazoo":
					homeIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.animazoo);
					break;

				case "aturmadoseulobato":
					homeIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.aturmadoseulobato);
					break;

				case "galinhapintadinha":
					homeIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.galinhapintadinha);
					break;

				case "nutriventures":
					homeIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.nutriventures);
					break;

				case "omnom":
					homeIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.omnom);
					break;

				case "diversos":
					homeIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.diversos);
					break;

				case "patatipatata":
					homeIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.patatipatata);
					break;

				case "peixonauta":
					homeIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.peixonauta);
					break;

				case "turmadoseulobato":
					homeIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.turmadoseulobato);
					break;

				case "turminhaparaiso":
					homeIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.turminhaparaiso);
					break;

				case "oshowdaluna":
					homeIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.oshowdaluna);
					break;

				case "ospequerruchos":
					homeIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.ospequerruchos);
					break;

				case "pepapigword":
					homeIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.pepapigword);
					break;

				case "trezpalavrinhas":
					homeIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.trezpalavrinhas);
					break;

				default:

					System.out.println("Este não uma categoria válida!");

			}

			gridArray.add(new Item(homeIcon));
		}

		gridView = (GridView) findViewById(R.id.gridView1);
		customGridAdapter = new CustomGridViewAdapter(this, R.layout.row_grid, gridArray);
		gridView.setAdapter(customGridAdapter);

		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				//Toast.makeText(MainActivity.this, "Click" + position + R.layout.row_grid, Toast.LENGTH_SHORT).show();

				Intent i = new Intent(MainActivity.this, VideosActivity.class);

				Bundle params = new Bundle();
				ArrayList<Video> videosDoPacote = dadosTela.getPacoteDeVideo().get(position).getVideo();

				params.putInt("visualizacoesDiarias", dadosTela.getVisualizacoesDiarias());
				params.putSerializable("videos", videosDoPacote);
				i.putExtras(params);

				startActivity(i);

			}
		});


	}

	private void registreRetornoFacebook(){
		shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
			@Override
			public void onSuccess(Sharer.Result result) {
				Log.i("Script", "Compartilhamento feito com sucesso! " + result);

				SimpleDateFormat formatador = new SimpleDateFormat("yyyyMMdd");
				VisualizacaoBD bd = new VisualizacaoBD(getBaseContext());
				int dataAAAAMMDD = Integer.parseInt(formatador.format(new Date()));
				int visualizacoesBd = bd.buscarQuantidadeDeVisualizacao(dataAAAAMMDD);
				int totalDeVisulizacoesNoDiaAtualizado = this.calcularQuantidadeDeVisualizacoes(dadosTela.getCompartilhamento(),
						visualizacoesBd);

				bd.atualizarQuantidadeDeVisualizacao(dataAAAAMMDD, totalDeVisulizacoesNoDiaAtualizado);
				int totalVisualizacoes = dadosTela.getVisualizacoesDiarias() - totalDeVisulizacoesNoDiaAtualizado;


				Toast.makeText(getApplicationContext(), "Obrigado! Você acaba de ter direito a novas visualizações totalizando: "
						+ totalVisualizacoes, Toast.LENGTH_SHORT).show();

				Log.i("Script", "Novo número de visualizações: " + totalVisualizacoes);

			}

			@Override
			public void onCancel() {
				Log.i("Script", "Compartilhamento cancelado pelo usuário!");
			}

			@Override
			public void onError(FacebookException e) {
				Log.i("Script", "Erro ao compartilhar o conteúdo! " + e.toString());
			}

			private int calcularQuantidadeDeVisualizacoes(int bonusCompartilhamento,
														  int totalDeVisualizacaoNoDia) {
				int quantidadeDeVisualizacoes = 0;


				if ((totalDeVisualizacaoNoDia - bonusCompartilhamento) > 0) {
					quantidadeDeVisualizacoes = totalDeVisualizacaoNoDia - bonusCompartilhamento;
				}

				return quantidadeDeVisualizacoes;
			}


		});

	}


	@Override
	public void onBackPressed() {

		try {

			new AlertDialog.Builder(this)
					.setTitle("Hora Da Diversão")
					.setMessage("Deseja realmente sair?")
					.setNegativeButton(android.R.string.no, null)
					.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface arg0, int arg1) {
							BackgroundSoundService.pauseMusic();
							stopService(serviceMusic);
							MainActivity.super.onBackPressed();
						}
					}).create().show();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private final Runnable handle = new Runnable() {
		public void run() {
			try {
				//array containing drawables ids
				int[] backgroundArray = new int[]{R.drawable.fundo01, R.drawable.fundo03, R.drawable.fundo04, R.drawable.fundo05};
				int backgroundId = 0;

				SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

				//obtendo ultimo background salvo
				backgroundId = preferences.getInt("backgroundId", 0);
				if(backgroundId > 1) backgroundId = 0;
				MainActivity.this.getWindow().setBackgroundDrawableResource(backgroundArray[backgroundId]);
				backgroundId++;

				//salvando ultimo background escolhido
				SharedPreferences.Editor editor = preferences.edit();
				editor.putInt("backgroundId", backgroundId);

				// Commit the edits!
				editor.commit();

			} catch (Exception e) {
				Log.d("Test", e.toString());
			}
		}
	};

	/*integracao com facebook*/

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		callbackManager.onActivityResult(requestCode, resultCode, data);
	}

	// metodo para compartilhamento
	public void shareContent(View view){

		if (ShareDialog.canShow(ShareLinkContent.class)) {
			ShareLinkContent linkContent = new ShareLinkContent.Builder()
					.setContentTitle("Hora da Diversão")
					.setContentDescription("Divirta-se com a criançada assistindo " +
							"os melhores vídeos musicais infantis inteiramente grátis em seu smartphone!")
					.setImageUrl(Uri.parse("https://dl.dropboxusercontent.com/s/97v4xlulicx5q2o/logofacebook.png?dl=0"))
					.setContentUrl(Uri.parse("http://developers.facebook.com/android"))
					.build();

			shareDialog.show(linkContent);
		}


	}

	public void startStopMusic(View view){

		ImageButton imgButton = (ImageButton) findViewById(R.id.speaker);

		if(BackgroundSoundService.isPlaying()) {
			imgButton.setBackgroundResource(R.drawable.speakermute);
			BackgroundSoundService.pauseMusic();
			this.salvarOpcaoBotaoStartStopMusic(false);
		}else{
			imgButton.setBackgroundResource(R.drawable.speakerup);
			BackgroundSoundService.startMusic();
			this.salvarOpcaoBotaoStartStopMusic(true);
		}

	}

	private void salvarOpcaoBotaoStartStopMusic(boolean executarMusica){
		SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean("executarMusica", executarMusica);

		editor.commit();
	}

	private boolean obterOpcaoBotaoStartStopMusic(){
		SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

		return preferences.getBoolean("executarMusica", true);
	}

	public void onResume() {
		super.onResume();
		Log.i("Musica: ", "Chamou doInBackground! ");
		if(this.obterOpcaoBotaoStartStopMusic()){
			BackgroundSoundService.startMusic();
		}

	}


	@Override
	protected void onStop() {
		super.onStop();
		Log.i("Musica: ", "Chamou onStop! ");
		MusicActivityControl.stopMusic(getSystemService(Context.ACTIVITY_SERVICE), getPackageName().toString());

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		BackgroundSoundService.pauseMusic();
		stopService(serviceMusic);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

	}

}

