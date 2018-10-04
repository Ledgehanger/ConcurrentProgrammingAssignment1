/*
 * Use command-line flag -ea for java VM to enable assertions.
 */
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.lang.Long;
import java.lang.Integer;
import java.io.File;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.StringTokenizer;
import java.util.concurrent.*;

abstract class SparseMatrix {
    int num_vertices; // Number of vertices in the graph
    int num_edges;    // Number of edges in the graph

    // Auxiliary in preparation of PageRank iteration: pre-calculate the
    // out-degree (number of outgoing edges) for each vertex
    abstract void calculateOutDegree( int outdeg[] );

    // Perform one PageRank iteration.
    //    a: damping factor
    //    in[]: previous PageRank values, read-only
    //    out[]: new PageRank values, initialised to zero
    //    outdeg[]: values pre-calculated by calculateOutDegree()
    abstract void iterate( double a, double[] in, double[] out, int outdeg[] );
}

// This class represents the adjacency matrix of a graph as a sparse matrix
// in coordinate format (COO)
class SparseMatrixCOO extends SparseMatrix {
    // TODO: variable declarations
    int [] source;
    int [] destination;

    SparseMatrixCOO( String file ) {
        try {
            InputStreamReader is
                    = new InputStreamReader( new FileInputStream( file ), "UTF-8" );
            BufferedReader rd = new BufferedReader( is );
            readFile( rd );
        } catch( FileNotFoundException e ) {
            System.err.println( "File not found: " + e );
            return;
        } catch( UnsupportedEncodingException e ) {
            System.err.println( "Unsupported encoding exception: " + e );
            return;
        } catch( Exception e ) {
            System.err.println( "Exception: " + e );
            return;
        }
    }

    int getNext( BufferedReader rd ) throws Exception {
        String line = rd.readLine();
        if( line == null )
            throw new Exception( "premature end of file" );
        return Integer.parseInt( line );
    }

    void getNextPair( BufferedReader rd, int pair[] ) throws Exception {
        String line = rd.readLine();
        if( line == null )
            throw new Exception( "premature end of file" );
        StringTokenizer st = new StringTokenizer( line );
        pair[0] = Integer.parseInt( st.nextToken() );
        pair[1] = Integer.parseInt( st.nextToken() );
    }

    void readFile( BufferedReader rd ) throws Exception {
        String line = rd.readLine();
        if( line == null )
            throw new Exception( "premature end of file" );
        if( !line.equalsIgnoreCase( "COO" ) )
            throw new Exception( "file format error -- header" );

        num_vertices = getNext(rd);
        num_edges = getNext(rd);

        // TODO: Allocate memory for the COO representation
        source = new int[num_edges];
	    destination = new int[num_edges];

        int edge[] = new int[2];
        for( int i=0; i < num_edges; ++i ) {
            getNextPair( rd, edge );
            // TODO:
            //    Insert edge with source edge[0] and destination edge[1]
            source[i] = edge[0];
            destination[i] = edge[1];
        }
    }

    // Auxiliary function for PageRank calculation
    void calculateOutDegree( int outdeg[] ) {
        // TODO:
        //    Calculate the out-degree for every vertex, i.e., the
        //    number of edges where a vertex appears as a source vertex.
        for(int i=0; i<num_edges; i++){
            outdeg[source[i]]++;
        }
    }

    void iterate( double a, double[] in, double[] out, int outdeg[] ) {
        // TODO:
        //    Iterate over all edges in the sparse matrix and calculate
        //    the contribution to the new PageRank value of a destination
        //    vertex made by the corresponding source vertex
        for(int i =0; i<num_vertices; i++) {
            if(outdeg[i] != 0) {
                for (int j = 0; j < outdeg[i]; j++) {
                    if(outdeg[j] != 0)
                        out[i] += a * (in[j] / outdeg[j]);
                    else
                        out[i] += a * in[j];
                }
            }
            else
                out[i] = a * in[i];

        }
    }
}

// This class represents the adjacency matrix of a graph as a sparse matrix
// in compressed sparse rows format (CSR), where a row index corresponds to
  class SparseMatrixCSR extends SparseMatrix {
    // TODO: variable declarations
    int [] source;

    SparseMatrixCSR( String file ) {
        try {
            InputStreamReader is
                    = new InputStreamReader( new FileInputStream( file ), "UTF-8" );
            BufferedReader rd = new BufferedReader( is );
            readFile( rd );
        } catch( FileNotFoundException e ) {
            System.err.println( "File not found: " + e );
            return;
        } catch( UnsupportedEncodingException e ) {
            System.err.println( "Unsupported encoding exception: " + e );
            return;
        } catch( Exception e ) {
            System.err.println( "Exception: " + e );
            return;
        }
    }

    int getNext( BufferedReader rd ) throws Exception {
        String line = rd.readLine();
        if( line == null )
            throw new Exception( "premature end of file" );
        return Integer.parseInt( line );
    }

    void readFile( BufferedReader rd ) throws Exception {
        String line = rd.readLine();
        if( line == null )
            throw new Exception( "premature end of file" );
        if( !line.equalsIgnoreCase( "CSR" ) && !line.equalsIgnoreCase( "CSC-CSR" ) )
            throw new Exception( "file format error -- header" );

        num_vertices = getNext(rd);
        num_edges = getNext(rd);

        // TODO: Allocate memory for the CSR representation
        source = new int [num_vertices];
	

        for( int i=0; i < num_vertices; ++i ) {
            line = rd.readLine();
            if( line == null )
                throw new Exception( "premature end of file" );
            String elm[] = line.split( " " );
            assert Integer.parseInt( elm[0] ) == i : "Error in CSR file";
            for( int j=1; j < elm.length; ++j ) {
                int dst = Integer.parseInt( elm[j] );
                // TODO:
                //    Record an edge from source i to destination dst

		
            }
        }
    }

    // Auxiliary function for PageRank calculation
    void calculateOutDegree( int outdeg[] ) {
        // TODO:
        //    Calculate the out-degree for every vertex, i.e., the
        //    number of edges where a vertex appears as a source vertex.
	
    }

    void iterate( double a, double[] in, double[] out, int outdeg_unused[] ) {
        // TODO:
        //    Iterate over all edges in the sparse matrix and calculate
        //    the contribution to the new PageRank value of a destination
        //    vertex made by the corresponding source vertex
        for(int i =0; i<num_vertices; i++) {
            if(outdeg_unused[i] != 0)
                out[i] += a * (in[i] / outdeg_unused[i]);

        }
    }
}

// This class represents the adjacency matrix of a graph as a sparse matrix
// in compressed sparse columns format (CSC). The incoming edges for each
// vertex are listed.
class SparseMatrixCSC extends SparseMatrix {
    // TODO: variable declarations
    

    SparseMatrixCSC( String file ) {
        try {
            InputStreamReader is
                    = new InputStreamReader( new FileInputStream( file ), "UTF-8" );
            BufferedReader rd = new BufferedReader( is );
            readFile( rd );
        } catch( FileNotFoundException e ) {
            System.err.println( "File not found: " + e );
            return;
        } catch( UnsupportedEncodingException e ) {
            System.err.println( "Unsupported encoding exception: " + e );
            return;
        } catch( Exception e ) {
            System.err.println( "Exception: " + e );
            return;
        }
    }

    int getNext( BufferedReader rd ) throws Exception {
        String line = rd.readLine();
        if( line == null )
            throw new Exception( "premature end of file" );
        return Integer.parseInt( line );
    }

    void readFile( BufferedReader rd ) throws Exception {
        String line = rd.readLine();
        if( line == null )
            throw new Exception( "premature end of file" );
        if( !line.equalsIgnoreCase( "CSC" ) && !line.equalsIgnoreCase( "CSC-CSR" ) )
            throw new Exception( "file format error -- header" );

        num_vertices = getNext(rd);
        num_edges = getNext(rd);

        // TODO: allocate data structures
	

        for( int i=0; i < num_vertices; ++i ) {
            line = rd.readLine();
            if( line == null )
                throw new Exception( "premature end of file" );
            String elm[] = line.split( " " );
            assert Integer.parseInt( elm[0] ) == i : "Error in CSC file";
            for( int j=1; j < elm.length; ++j ) {
                int src = Integer.parseInt( elm[j] );
                // TODO:
                //    Record an edge from source src to destination i
		
            }
        }
    }

    // Auxiliary function for PageRank calculation
    void calculateOutDegree( int outdeg[] ) {
        // TODO:
        //    Calculate the out-degree for every vertex, i.e., the
        //    number of edges where a vertex appears as a source vertex.
	
    }

    void iterate( double a, double[] in, double[] out, int outdeg[] ) {
        // TODO:
        //    Iterate over all edges in the sparse matrix and calculate
        //    the contribution to the new PageRank value of a destination
        //    vertex made by the corresponding source vertex
        for(int i =0; i<num_vertices; i++) {
            if(outdeg[i] != 0)
                out[i] += a * (in[i] / outdeg[i]);

        }
	
    }
}

// Main class with main() method. Performs the PageRank computation until
// convergence is reached.
class PageRank {
    public static void main( String args[] ) {
        if( args.length < 3 ) {
            System.err.println( "Usage: java pagerank format inputfile outputfile" );
            return;
        }

        String format = args[0];
        String inputFile = args[1];
        String outputFile = args[2];

        // Tell us what you're doing
        System.err.println( "Format: " + format );
        System.err.println( "Input file: " + inputFile );
        System.err.println( "Output file: " + outputFile );

        long tm_start = System.nanoTime();

        SparseMatrix matrix;

        if( format.equalsIgnoreCase( "CSR" ) ) {
            matrix = new SparseMatrixCSR( inputFile );
        } else if( format.equalsIgnoreCase( "CSC" ) ) {
            matrix = new SparseMatrixCSC( inputFile );
        } else if( format.equalsIgnoreCase( "COO" ) ) {
            matrix = new SparseMatrixCOO( inputFile );
        } else {
            System.err.println( "Unknown format '" + format + "'" );
            return;
        }

        double tm_input = (double)(System.nanoTime() - tm_start) * 1e-9;
        System.err.println( "Reading input: " + tm_input + " seconds" );
        tm_start = System.nanoTime();

        final int n = matrix.num_vertices;
        double x[] = new double[n];
        double v[] = new double[n];
        double y[] = new double[n];
        final double d = 0.85; // Leave this value as is
        final double tol = 1e-7; // Leave this value as is
        final int max_iter = 100;
        final boolean verbose = true;
        double delta = 2;
        int iter = 0;

        for( int i=0; i < n; ++i ) {
            x[i] = v[i] = 1.0 / (double)n;
            y[i] = 0;
        }

        int outdeg[] = new int[n];
        matrix.calculateOutDegree( outdeg );

        double tm_init = (double)(System.nanoTime() - tm_start) * 1e-9;
        System.err.println( "Initialisation: " + tm_init + " seconds" );
        tm_start = System.nanoTime();

        while( iter < max_iter && delta > tol ) {
            // Power iteration step.
            // 1. Transfering weight over out-going links (summation part)
            matrix.iterate( d, x, y, outdeg );
            // 2. Constants (1-d)v[i] added in separately.
            double w = 1.0 - sum( y, n ); // ensure y[] will sum to 1
            for( int i=0; i < n; ++i )
                y[i] += w * v[i];

            // Calculate residual error
            delta = normdiff( x, y, n );
            iter++;

            // Swap x[] and y[] and reset y[]
            for( int i=0; i < n; ++i ) {
                x[i] = y[i];
                y[i] = 0.;
            }

            double tm_step = (double)(System.nanoTime() - tm_start) * 1e-9;
            if( verbose )
                System.err.println( "iteration " + iter + ": delta=" + delta
                        + " xnorm=" + sum(x, n)
                        + " time=" + tm_step + " seconds" );
            tm_start = System.nanoTime();
        }

        if( delta > tol )
            System.err.println( "Error: solution has not converged." );

        // Dump PageRank values to file
        writeToFile( outputFile, x, n );
    }

    static double sum( double[] a, int n ) {
        double d = 0.;
        double err = 0.;
        for( int i=0; i < n; ++i ) {
            // The code below achieves
            // d += a[i];
            // but does so with high accuracy
            double tmp = d;
            double y = a[i] + err;
            d = tmp + y;
            err = tmp - d;
            err += y;
        }
        return d;
    }

    static double normdiff( double[] a, double[] b, int n ) {
        double d = 0.;
        double err = 0.;
        for( int i=0; i < n; ++i ) {
            // The code below achieves
            // d += Math.abs(b[i] - a[i]);
            // but does so with high accuracy
            double tmp = d;
            double y = Math.abs( b[i] - a[i] ) + err;
            d = tmp + y;
            err = tmp - d;
            err += y;
        }
        return d;
    }

    static void writeToFile( String file, double[] v, int n ) {
        try {
            OutputStreamWriter os
                    = new OutputStreamWriter( new FileOutputStream( file ), "UTF-8" );
            BufferedWriter wr = new BufferedWriter( os );
            writeToBuffer( wr, v, n );
        } catch( FileNotFoundException e ) {
            System.err.println( "File not found: " + e );
            return;
        } catch( UnsupportedEncodingException e ) {
            System.err.println( "Unsupported encoding exception: " + e );
            return;
        } catch( Exception e ) {
            System.err.println( "Exception: " + e );
            return;
        }
    }
    static void writeToBuffer( BufferedWriter buf, double[] v, int n ) {
        PrintWriter out = new PrintWriter( buf );
        for( int i=0; i < n; ++i )
            out.println( i + " " + v[i] );
        out.close();
    }
}
