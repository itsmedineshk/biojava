/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.biojava3.genome;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Collection;
import java.util.LinkedHashMap;
import junit.framework.TestCase;
import junitx.framework.FileAssert;
import org.biojava3.core.sequence.ChromosomeSequence;
import org.biojava3.core.sequence.GeneSequence;
import org.biojava3.core.sequence.ProteinSequence;
import org.biojava3.core.sequence.io.FastaWriterHelper;
import org.biojava3.genome.parsers.gff.GFF3Writer;

/**
 *
 * @author Scooter Willis <willishf at gmail dot com>
 */
public class GeneFeatureHelperTest extends TestCase {

    public GeneFeatureHelperTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of loadFastaAddGeneFeaturesFromUpperCaseExonFastaFile method, of class GeneFeatureHelper.
     * @throws Exception
     */

    public void testLoadFastaAddGeneFeaturesFromUpperCaseExonFastaFile() throws Exception{
        System.out.println("loadFastaAddGeneFeaturesFromUpperCaseExonFastaFile");
        File fastaSequenceFile = new File("src/test/resources/volvox_all.fna");
        File uppercaseFastaFile = new File("src/test/resources/volvox_all_genes_exon_uppercase.faa");
        boolean throwExceptionGeneNotFound = false;
        LinkedHashMap<String, ChromosomeSequence> chromosomeSequenceHashMap = GeneFeatureHelper.loadFastaAddGeneFeaturesFromUpperCaseExonFastaFile( fastaSequenceFile, uppercaseFastaFile,  throwExceptionGeneNotFound);

        FileOutputStream fo = new FileOutputStream("src/test/resources/testoutput/volvox_all_genes_exon_uppercase.gff3");
        GFF3Writer gff3Writer = new GFF3Writer();
        gff3Writer.write(fo, chromosomeSequenceHashMap);
        fo.close();

    }

    /**
     * Test of outputFastaSequenceLengthGFF3 method, of class GeneFeatureHelper.
     */
    public void testOutputFastaSequenceLengthGFF3() throws Exception {
        System.out.println("outputFastaSequenceLengthGFF3");

        File fastaSequenceFile = new File("src/test/resources/volvox_all.fna");
        File gffFile = new File("src/test/resources/testoutput/volvox_length.gff3");
        GeneFeatureHelper.outputFastaSequenceLengthGFF3(fastaSequenceFile, gffFile);
        FileAssert.assertBinaryEquals("volvox_length.gff3 and volvox_length_output.gff3 are not equal", gffFile, new File("src/test/resources/volvox_length_reference.gff3"));

    }

    /**
     * Test of getProteinSequences method, of class GeneFeatureHelper. Used gff3 file that was modified from the volvox gff version. Do not
     * have the reference protein that is generated from each CDS record so subject to being incorrect without a validated test case.
     * Could not find anyone providing a gff3 test case with expected protein output.
     */
    public void testGetProteinSequences() throws Exception {
        System.out.println("getProteinSequences");
        LinkedHashMap<String, ChromosomeSequence> chromosomeSequenceList = GeneFeatureHelper.loadFastaAddGeneFeaturesFromGmodGFF3(new File("src/test/resources/volvox_all.fna"), new File("src/test/resources/volvox.gff3"));
        LinkedHashMap<String, ProteinSequence> proteinSequenceList = GeneFeatureHelper.getProteinSequences(chromosomeSequenceList.values());
        FastaWriterHelper.writeProteinSequence(new File("src/test/resources/testoutput/volvox_all.faa"), proteinSequenceList.values());
        FileAssert.assertBinaryEquals("volvox_all_reference.faa and volvox_all.faa are not equal", new File("src/test/resources/volvox_all_reference.faa"), new File("src/test/resources/testoutput/volvox_all.faa"));

    }

    /**
     * Test of getGeneSequences method, of class GeneFeatureHelper.
     */
    public void testGetGeneSequences() throws Exception {
        System.out.println("getGeneSequences");
        LinkedHashMap<String, ChromosomeSequence> chromosomeSequenceList = GeneFeatureHelper.loadFastaAddGeneFeaturesFromGmodGFF3(new File("src/test/resources/volvox_all.fna"), new File("src/test/resources/volvox.gff3"));
        LinkedHashMap<String, GeneSequence> geneSequenceHashMap = GeneFeatureHelper.getGeneSequences(chromosomeSequenceList.values());
        Collection<GeneSequence> geneSequences = geneSequenceHashMap.values();
        FastaWriterHelper.writeGeneSequence(new File("src/test/resources/volvox_all_genes_exon_uppercase.faa"), geneSequences, true);
    }


}