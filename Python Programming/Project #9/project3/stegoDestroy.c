//
// Stego --- insert info in a BMP image file
//

#include "stego.h"

int main(int argc, const char *argv[])
{
    FILE *in,
         *indata,
         *out;
    
    int i,
        j,
        x,
        ttt,
        shft,
        byteCount,
        moreData,
        moreImage,
        imageBytes,
        dataBytes = 10000,
        dataBytesWritten,
        imageBytesWritten;

    char temp;
    char data = 'A';

    char infname[80],
         outfname[80];

    if(argc != 3)
    {
oops:   fprintf(stderr, "\nUsage: %s plainImage stegoImage stegoData\n\n", argv[0]);
        fprintf(stderr, "where plainImage == filename containing initial image\n");
        fprintf(stderr, "      stegoImage == filename for image containing stego data\n");
        exit(0);
    }
    
    sprintf(infname, argv[1]);
    sprintf(outfname, argv[2]);

    in = fopen(infname, "r");
    if(in == NULL)
    {
        fprintf(stderr, "\nError opening file %s\n\n", infname);
        exit(0);
    }

    imageBytes = 0;
    while(1)
    {
        x = fscanf(in, "%c", &temp);
        if(x != 1)
        {
            break;
        }
        ++imageBytes;
    }
    fclose(in);
    
    printf("image bytes = %d, capacity = %d bytes\n", imageBytes, imageBytes >> 3);
    
    in = fopen(infname, "r");
    if(in == NULL)
    {
        fprintf(stderr, "\nError opening file %s\n\n", infname);
        exit(0);
    }

    
    printf("dataBytes = %d\n", dataBytes);
    

    out = fopen(outfname, "w");
    if(out == NULL)
    {
        fprintf(stderr, "\nError opening file %s\n\n", outfname);
        exit(0);
    }

    //
    // skip first START_FROM bytes of image file
    //
    imageBytesWritten = 0;
    for(i = 0; i < START_FROM; ++i)
    {
        x = fscanf(in, "%c", &temp);
        if(x != 1)
        {
            fprintf(stderr, "\nError in file %s\n\n", infname);
            exit(0);
        }
        fprintf(out, "%c", temp);
        ++imageBytesWritten;
    }

    /*//
    // insert 64 bits of the form 0xa5
    // to indicate that the file contains stego data
    //
    ttt = 0xa5;
    for(i = 0; i < 8; ++i)
    {
        for(j = 0; j < 8; ++j)
        {
            x = fscanf(in, "%c", &temp);
            if(x != 1)
            {
                fprintf(stderr, "\nError in file %s\n\n", infname);
                exit(0);
            }
            temp = (temp & 0xfe) ^ ((ttt >> j) & 0x1);
            fprintf(out, "%c", temp);
            ++imageBytesWritten;
        }
    }*/

    //
    // read 64 bits of the file
    // if not of the form 0xa5, then
    // file does not contains stego data
    //
    for(i = 0; i < 8; ++i)
    {
        ttt = 0x0;
        for(j = 0; j < 8; ++j)
        {
            x = fscanf(in, "%c", &temp);
            if(x != 1)
            {
                fprintf(stderr, "\nError in file %s\n\n", infname);
                exit(0);
            }
            ttt ^= ((temp & 0x1) << j);
	    temp = (temp & 0xfe) ^ ((0xa5 >> j) & 0x1);
            fprintf(out, "%c", temp);
            ++imageBytesWritten;
        }
        if(ttt != 0xa5)
        {
            fprintf(stderr, "\nError --- file does not contain stego data that I can read\n\n");
            exit(0);
        }

    }
    
    //
    // insert bits of the number dataBytes (27 bits)
    //
    for(i = 0; i < 27; ++i)
    {
        x = fscanf(in, "%c", &temp);
        if(x != 1)
        {
            fprintf(stderr, "\nError in file %s\n\n", infname);
            exit(0);
        }
        temp = (temp & 0xfe) ^ ((dataBytes >> i) & 0x1);
        fprintf(out, "%c", temp);
        ++imageBytesWritten;
    }

    
    moreImage = 1;
    shft = 0;
    dataBytesWritten = 0;
    
    while(moreImage)
    {
//        printf("byteCount = %d\n", byteCount);
        
        x = fscanf(in, "%c", &temp);
        if(x != 1)
        {
            moreImage = 0;
        }
        else
        {
            if(dataBytesWritten < dataBytes)
            {
                temp = (temp & 0xfe) ^ ((data >> shft) & 0x1);
                shft = (shft + 1) & 0x7;
            
                if(shft == 0)
                {
                    ++dataBytesWritten;

/***
                    printf("data = %c = ", data);
                    for(i = 0; i < 8; ++i)
                    {
                        printf("%1d", (data >> i) & 0x1);
                    }
                    printf("\n");
***/
                }

            }// end if

            fprintf(out, "%c", temp);
            ++imageBytesWritten;

        }// end if

    }// end while
    
    printf("\nimage bytes written = %d, data bytes written = %d\n", 
        imageBytesWritten, dataBytesWritten);

    printf("\n");

    fclose(in);
    fclose(out);

}// end main
