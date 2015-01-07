import java.util.Arrays;
public class Denoiser {

    public Denoiser() {
    }

    /**
     * Windows sampled signal using overlapping Hamming windows
     * @param ss The sampled signal
     * @param ww The window width
     * @param or The overlap ratio
     * @return seg The overlapping windowed segments
     */

    private static double[][] segmentSignal(double[] ss, int ww, double or ) {
        int len = ss.length;
        double d = 1 - or;
        int frames = (int)(Math.floor((len - d) / ww / d));
        int start = 0;
        int stop = 0;
        double[] window = Utils.hamming(ww);

        double[][] seg = new double[ww][frames];

        for (int i = 0; i < frames; i++) {
            start = (int)(i * ww * or );
            stop =  start + ww;

            for (int k = 0; k < ww; k++) {
                // seg[k][i] = ss[start + k] * window[k];
                seg[k][i] = ss[start + k];
            }
        }
        return seg;
    }

/**
 * Overlap and add segments to calculate reconstructed signal
 * @param  segments 2D array of overlapping signal segments
 * @param  or overlap ratio
 * @return   reconstructedSignal Speech signal post speech denoising
 */

    private static double[] overlapAndAdd(double[][] segments, double or ) {
        int ww = segments.length;
        int frames = segments[0].length;
        int start = 0;
        int stop = 0;
        int signalLength = (int)(ww * (1 - or ) * (frames - 1) + ww);

        double[] reconstructedSignal = new double[signalLength];

        for (int i = 0; i < frames; i++) {
            start = (int)(i * ww * or );
            stop =  start + ww;
            for (int k = 0; k < ww; k++) {
               reconstructedSignal[start+k] = reconstructedSignal[start+k] + segments[k][i];
            }
         }
        return reconstructedSignal;
    }

    public static void main(String[] args) {
        double[] test = {1, 2, 3,4, 5, 6, 7,8, 9, 10, 11, 12};

        double[][] stack = segmentSignal(test, 4, 0.5);
        double[] recon = overlapAndAdd(stack,0.5);


	System.out.println(Arrays.toString(test));
        System.out.println(Arrays.deepToString(stack));


        System.out.println(Arrays.toString(recon));
    }
}