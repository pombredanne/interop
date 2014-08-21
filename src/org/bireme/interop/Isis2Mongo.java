/*=========================================================================

    Copyright © 2014 BIREME/PAHO/WHO

    This file is part of Interop.

    Interop is free software: you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public License as
    published by the Free Software Foundation, either version 2.1 of
    the License, or (at your option) any later version.

    Interop is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with Interop. If not, see <http://www.gnu.org/licenses/>.

=========================================================================*/

package org.bireme.interop;

import bruma.BrumaException;
import bruma.master.Master;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.bireme.interop.fromJson.Json2Mongo;
import org.bireme.interop.toJson.Isis2Json;

/**
 *
 * @author Heitor Barbieri
 * date: 20140812
 */
public class Isis2Mongo extends Source2Destination {
    public Isis2Mongo(final Isis2Json i2j,
                      final Json2Mongo j2m) {
        super(i2j, j2m);
    }
    
    private static void usage() {
        System.err.println("usage: Isis2Mongo <isismst> <mongohost> <mongodb> " 
                                                        + "<mongocol> OPTIONS");
        System.err.println();
        System.err.println("       <isismst> - Isis master file name.");
        System.err.println("       <mongohost> - MongoDB server url.");
        System.err.println("       <mongodb> - MongoDB database.");
        System.err.println("       <mongocol> - MongoDB colection name.");
        System.err.println();
        System.err.println("OPTIONS:");
        System.err.println();
        System.err.println("       --isisencoding=<encod>");
        System.err.println("           Encoding of the isis records.");
        System.err.println("       --isistags=<num>,<num>,<num>,...");
        System.err.println("           Isis field tags to be exported (comma separated).");
        System.err.println("       --isisfrom=<mfn>");
        System.err.println("           Initial mfn to be exported.");
        System.err.println("       --isisto=<mfn>");
        System.err.println("           Last mfn to be exported.");
        System.err.println("       --mongoport=<port>");
        System.err.println("           MongoDb server port.");
        System.err.println("       --mongouser=<user>");
        System.err.println("           MongoDB server user.");
        System.err.println("       --mongopsw=<password>");   
        System.err.println("           MongoDB server password.");
        System.err.println("       --convtable=<file>");
        System.err.println("           Convertion table file name used"
                      + "to convert Isis record tag into MongoDB field tag.");
        System.err.println("           One convertion per line of type: " 
                                         + "<field_tag_num>=<mongodb_tag_str>");
        System.err.println("       --tell=<num>");
        System.err.println("           Outputs log message each <num> exported documents.");
        System.err.println("       --append");        
        System.err.println("           Appends the documents into the destination MongoDB colection.");
        
        System.exit(1);
    }
    
    public static void main(final String[] args) throws BrumaException, 
                                                        IOException {
        final int len = args.length;
        if (len < 4) {
            usage();
        }
        
        final String mstname = args[0];
        final String mongoHost = args[1];
        final String mongoDbName = args[2];
        final String mongoColName = args[3];
        
        String encoding = Master.GUESS_ISO_IBM_ENCODING;
        List<Integer> tags = null;
        int from = 1;
        int to = Integer.MAX_VALUE;
        
        String mongoPort = Integer.toString(Json2Mongo.DEFAULT_MONGO_PORT);
        String mongoUser = null;
        String mongoPswd = null;
        String convTable = null;
        
        boolean append = false;
        int tell = Integer.MAX_VALUE;
        
        for (int idx = 4; idx < len; idx++) {
            if (args[idx].startsWith("--isisencoding=")) {
                encoding = args[idx].substring(15);
            } else if (args[idx].startsWith("--isistags=")) {
                final String stags = args[idx].substring(11);
                tags = new ArrayList<>();
                for (String stag : stags.split("=")) {
                    tags.add(Integer.parseInt(stag));
                }
            } else if (args[idx].startsWith("--isisfrom=")) {
                from = Integer.parseInt(args[idx].substring(11));
            } else if (args[idx].startsWith("--isisto=")) {
                to = Integer.parseInt(args[idx].substring(9));
            } else if (args[idx].startsWith("--mongoport=")) {   
                mongoPort = args[idx].substring(12);
            } else if (args[idx].startsWith("--mongouser=")) {
                mongoUser = args[idx].substring(12);
            } else if (args[idx].startsWith("--mongopsw=")) {
                mongoPswd = args[idx].substring(12);
            } else if (args[idx].startsWith("--convtable=")) {
                convTable = args[idx].substring(12);
            } else if (args[idx].startsWith("--tell=")) {
                tell = Integer.parseInt(args[idx].substring(7));
            } else if (args[idx].equals("--append")) {
                append = true;
            } else {
                usage();
            }
        }
        
        final Isis2Json i2j = new Isis2Json(mstname,
                                            encoding,
                                            tags,
                                            from,
                                            to,
                                            convTable);
        final Json2Mongo j2m = new Json2Mongo(mongoHost,
                                              mongoPort,
                                              mongoUser,
                                              mongoPswd,
                                              mongoDbName,
                                              mongoColName,
                                              append);
        
        new Isis2Mongo(i2j, j2m).export(tell);
    }
}
