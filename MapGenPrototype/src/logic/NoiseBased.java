package logic;

import java.util.Random;

public class NoiseBased implements ProceduralAlgorithm {

	private String name;
	private boolean hasMap;
	private int[][] map;
	private int mapSize;
	private double[][] noiseMap;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int[][] getMap() {
		return map;
	}

	@Override
	public boolean mapPresent() {
		return hasMap;
	}

	@Override
	public int[][] generateMap(int size) {
		this.mapSize = size;
		noiseMap = noise(size, size);
		map = new int[size][size];
		hasMap = true;
		//GeneratePerlinNoise(noiseMap, 7);
		//new Util().printMap(map);
		createHeightMap();
		
		return map;
	}
	
	private void createHeightMap() {
		SimplexNoise simplexNoise = new SimplexNoise(100,0.1,5000);
		
		double xStart=0;
	    double XEnd=500;
	    double yStart=0;
	    double yEnd=500;

	    double[][] result = new double[mapSize][mapSize];

	    for(int i=0;i<mapSize;i++){
	        for(int j=0;j<mapSize;j++){
	            int x=(int)(xStart+i*((XEnd-xStart)/(mapSize*45)));
	            int y=(int)(yStart+j*((yEnd-yStart)/(mapSize*45)));
	            result[i][j]=0.5*(1+simplexNoise.getNoise(x,y));
	        }
	    }

	   new Util().printMap(result);
	}

	double[][] noise(int width, int height) {
		Random random = new Random(50); // Seed to 0 for testing
		noiseMap = new double[mapSize][mapSize];

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				noiseMap[i][j] = (random.nextDouble() % 1 * 100);
			}
		}

		return noiseMap;
	}

	double[][] GeneratePerlinNoise(double[][] noiseMap, int octaveCount) {
		int width = noiseMap.length;
		int height = noiseMap[0].length;

		double[][][] smoothNoise = new double[octaveCount][][]; // an array of 2D arrays containing

		double persistance = 0.5f;

		// generate smooth noise
		for (int i = 0; i < octaveCount; i++) {
			smoothNoise[i] = GenerateSmoothNoise(noiseMap, i);
		}

		double[][] perlinNoise = new double[mapSize][mapSize];
		double amplitude = 1.0f;
		double totalAmplitude = 0.0f;

		// blend noise together
		for (int octave = octaveCount - 1; octave >= 0; octave--) {
			amplitude *= persistance;
			totalAmplitude += amplitude;

			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					perlinNoise[i][j] += smoothNoise[octave][i][j] * amplitude;
				}
			}
		}

		// normalisation
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				map[i][j] = (int) (perlinNoise[i][j] / totalAmplitude);
			}
		}

		return perlinNoise;
	}

	double[][] GenerateSmoothNoise(double[][] noiseMap, int octave) {
		int width = noiseMap.length;
		int height = noiseMap[0].length;

		double[][] smoothNoise = noiseMap;

		int samplePeriod = 1 << octave; // calculates 2 ^ k
		double sampleFrequency = 1.0f / samplePeriod;

		for (int i = 0; i < noiseMap.length; i++) {
			// calculate the horizontal sampling indices
			int sample_i0 = (i / samplePeriod) * samplePeriod;
			int sample_i1 = (sample_i0 + samplePeriod) % width; // wrap around
			double horizontal_blend = (i - sample_i0) * sampleFrequency;

			for (int j = 0; j < noiseMap.length; j++) {
				// calculate the vertical sampling indices
				int sample_j0 = (j / samplePeriod) * samplePeriod;
				int sample_j1 = (sample_j0 + samplePeriod) % height; // wrap around
				double vertical_blend = (j - sample_j0) * sampleFrequency;

				// blend the top two corners
				double top = Interpolate(noiseMap[sample_i0][sample_j0], noiseMap[sample_i1][sample_j0],
						horizontal_blend);

				// blend the bottom two corners
				double bottom = Interpolate(noiseMap[sample_i0][sample_j1], noiseMap[sample_i1][sample_j1],
						horizontal_blend);

				// final blend
				smoothNoise[i][j] = Interpolate(top, bottom, vertical_blend);
			}
		}

		return smoothNoise;
	}

	double Interpolate(double x0, double x1, double alpha) {
		return x0 * (1 - alpha) + alpha * x1;
	}

	private int[][] createTemperatureMap() {
		return null;
	}

}
