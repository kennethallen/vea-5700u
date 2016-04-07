package hvcs;

// Huoth Tan, Michael Bernard
// CSCI 5700U
// Dr. Amar Rasheed
// DES algorithm implementation
// 4 April 2016

public class DES {
    int k1[] = new int[48], k2[] = new int[48], k3[] = new int[48],
        k4[] = new int[48], k5[] = new int[48], k6[] = new int[48],
        k7[] = new int[48], k8[] = new int[48],	k9[] = new int[48],
        k10[] = new int[48], k11[] = new int[48], k12[] = new int[48],
        k13[] = new int[48], k14[] = new int[48], k15[] = new int[48],
        k16[] = new int[48];
    
    // Store keys
    void storeKeys(int key[], int round) {
        int i;
        switch (round) {
            case 1:
                for (i = 0; i < 48; i++) {
                    k1[i] = key[i];
                }
                break;
            case 2:
                for (i = 0; i < 48; i++) {
                    k2[i] = key[i];
                }
                break;
            case 3:
                for (i = 0; i < 48; i++) {
                    k3[i] = key[i];
                }
                break;
            case 4:
                for (i = 0; i < 48; i++) {
                    k4[i] = key[i];
                }
                break;
            case 5:
                for (i = 0; i < 48; i++) {
                    k5[i] = key[i];
                }
                break;
            case 6:
                for (i = 0; i < 48; i++) {
                    k6[i] = key[i];
                }
                break;
            case 7:
                for (i = 0; i < 48; i++) {
                    k7[i] = key[i];
                }
                break;
            case 8:
                for (i = 0; i < 48; i++) {
                    k8[i] = key[i];
                }
                break;
            case 9:
                for (i = 0; i < 48; i++) {
                    k9[i] = key[i];
                }
                break;
            case 10:
                for (i = 0; i < 48; i++) {
                    k10[i] = key[i];
                }
                break;
            case 11:
                for (i = 0; i < 48; i++) {
                    k11[i] = key[i];
                }
                break;
            case 12:
                for (i = 0; i < 48; i++) {
                    k12[i] = key[i];
                }
                break;
            case 13:
                for (i = 0; i < 48; i++) {
                    k13[i] = key[i];
                }
                break;
            case 14:
                for (i = 0; i < 48; i++) {
                    k14[i] = key[i];
                }
                break;
            case 15:
                for (i = 0; i < 48; i++) {
                    k15[i] = key[i];
                }
                break;
            case 16:
                for (i = 0; i < 48; i++) {
                    k16[i] = key[i];
                }
                break;
        }
    }
    
    // XOR key with expanded right half of message
    int[] xorKE(int K[], int ER[]) {
	for (int i = 0; i < 48; i++) {
            if ((K[i] + ER[i]) % 2 == 0)
                ER[i] = 0;
            else
                ER[i] = 1;
        }
        
        return ER;
    }
    
    // XOR left half of message with right half after round method
    int[] xorLF(int L[], int f[]) {
	for (int i = 0; i < 32; i++) {
            if ((L[i] + f[i]) % 2 == 0)
                f[i] = 0;
            else
                f[i] = 1;
	}
        
        return f;
    }
    
    // Initial Permutation
    void IP(int m[], int L[], int R[]) {
	int[] tempM = new int[64];
	int[] ip =
	{
            58,50,42,34,26,18,10,2,
            60,52,44,36,28,20,12,4,
            62,54,46,38,30,22,14,6,
            64,56,48,40,32,24,16,8,
            57,49,41,33,25,17,9,1,
            59,51,43,35,27,19,11,3,
            61,53,45,37,29,21,13,5,
            63,55,47,39,31,23,15,7
	};

	// Permute message
	for (int i = 0; i < 64; i++)
            tempM[i] = m[ip[i] - 1];

	for (int i = 0; i < 32; i++)
            L[i] = tempM[i];

	for (int i = 0; i < 32; i++)
            R[i] = tempM[i + 32];
    }
    
    // Permutation Choice 1 (for key)
    int[] PC1(int key[]) {
	int[] tempKey = new int[56];
	int pc1[] =
	{
            57,49,41,33,25,17,9,
            1,58,50,42,34,26,18,
            10,2,59,51,43,35,27,
            19,11,3,60,52,44,36,
            63,55,47,39,31,23,15,
            7,62,54,46,38,30,22,
            14,6,61,53,45,37,29,
            21,13,5,28,20,12,4
	};

	// Permute key and compress to 56 bits
	for (int i = 0; i < 56; i++)
            tempKey[i] = key[pc1[i] - 1];

	for (int i = 0; i < 56; i++)
            key[i] = tempKey[i];
        
	return key;
    }
    
    // Permutation Choice 2 (for key)
    int[] PC2(int key[]) {
	int[] tempKey = new int[56];
	int pc2[] =
	{
            14,17,11,24,1,5,
            3,28,15,6,21,10,
            23,19,12,4,26,8,
            16,7,27,20,13,2,
            41,52,31,37,47,55,
            30,40,51,45,33,48,
            44,49,39,56,34,53,
            46,42,50,36,29,32
	};
        
	// Permute key and compress to 48 bits
	for (int i = 0; i < 48; i++)
            tempKey[i] = key[pc2[i] - 1];
	for (int i = 0; i < 48; i++)
            key[i] = tempKey[i];
        
	return key;
    }
    
    // Shift key left by rounds
    void shiftL(int C[], int D[], int round) {
	int[] tmpC = new int[28]; // temp for C0
	int[] tmpD = new int[28]; // temp for D0

        // One bitwise left shift if round 1,2,9 or 16
	if (round == 1 || round == 2 || round == 9 || round == 16) {
            int c1 = C[0]; // holding first # in C0
            int d1 = D[0]; // holding first # in D0
                
            // Shifting left
            for (int i = 1; i < 28; i++) {
                tmpC[i - 1] = C[i]; // tmpC[0] = C[1]
                tmpD[i - 1] = D[i]; // tmpD[0] = D[1]
            }
                
            tmpC[27] = c1; // tmpC[27] = first # in C0
            tmpD[27] = d1; // tmpC[27] = first # in D0

            // Putting tmpC and tmpD back into C0, D0
            for (int i = 0; i < 28; i++) {
                C[i] = tmpC[i];
                D[i] = tmpD[i];
            }
	}
        
	else {
            int c1[] = { C[0], C[1] };// holding first 2 # in C0
            int d1[] = { D[0], D[1] };// holding first 2 # in D0
            
            for (int i = 2; i < 28; i++) {// shifting left
		tmpC[i - 2] = C[i]; // tmpC[0] = C[1]
		tmpD[i - 2] = D[i]; // tmpD[0] = D[1]
            }
            
            tmpC[26] = c1[0]; // tmpC[27] = first # in C0
            tmpD[26] = d1[0]; // tmpC[27] = first # in D0
            tmpC[27] = c1[1]; // tmpC[27] = second # in C0
            tmpD[27] = d1[1]; // tmpC[27] = second # in D0

            // Putting tmpC and tmpD back in to C0, D0
            for (int i = 0; i < 28; i++) {
		C[i] = tmpC[i];
		D[i] = tmpD[i];
            }
	}
    }
    
    // Expand right half of message from 32 bits to 48 bits
    int[] expand(int R[], int ER[]) {
	int[] e =
	{
            32,1,2,3,4,5,
            4,5,6,7,8,9,
            8,9,10,11,12,13,
            12,13,14,15,16,17,
            16,17,18,19,20,21,
            20,21,22,23,24,25,
            24,25,26,27,28,29,
            28,29,30,31,32,1
	};
        
	for (int i = 0; i < 48; i++)
            ER[i] = R[e[i] - 1];
        
	return ER;
    }
    
    int[] permute(int sER[]) {
	int[] tempsER = new int[32];
	int[] p =
	{
            16,7,20,21,29,12,28,17,
            1,15,23,26,5,18,31,10,
            2,8,24,14,32,27,3,9,
            19,13,30,6,22,11,4,25
	};
        
	for (int i = 0; i < 32; i++)
            tempsER[i] = sER[p[i] - 1];
	for (int i = 0; i < 32; i++)
            sER[i] = tempsER[i];
        
	return sER;
    }
    
    int[] sub(int ER[], int sER[]) {
	int[][] s1 =
        {
            {14,4,13,1,2,15,11,8,3,10,6,12,5,9,0,7},
            {0,15,7,4,14,2,13,1,10,6,12,11,9,5,3,8},
            {4,1,14,8,13,6,2,11,15,12,9,7,3,10,5,0},
            {15,12,8,2,4,9,1,7,5,11,3,14,10,0,6,13}
	};

	int[][] s2 =
	{
            {15,1,8,14,6,11,3,4,9,7,2,13,12,0,5,10},
            {3,13,4,7,15,2,8,14,12,0,1,10,6,9,11,5},
            {0,14,7,11,10,4,13,1,5,8,12,6,9,3,2,15},
            {13,8,10,1,3,15,4,2,11,6,7,12,0,5,14,9}
	};

	int[][] s3 =
	{
            {10,0,9,14,6,3,15,5,1,13,12,7,11,4,2,8},
            {13,7,0,9,3,4,6,10,2,8,5,14,12,11,15,1},
            {13,6,4,9,8,15,3,0,11,1,2,12,5,10,14,7},
            {1,10,13,0,6,9,8,7,4,15,14,3,11,5,2,12}
	};

	int[][] s4 =
	{
            {7,13,14,3,0,6,9,10,1,2,8,5,11,12,4,15},
            {13,8,11,5,6,15,0,3,4,7,2,12,1,10,14,9},
            {10,6,9,0,12,11,7,13,15,1,3,14,5,2,8,4},
            {3,15,0,6,10,1,13,8,9,4,5,11,12,7,2,14}
	};

	int[][] s5 =
	{
            {2,12,4,1,7,10,11,6,8,5,3,15,13,0,14,9},
            {14,11,2,12,4,7,13,1,5,0,15,10,3,9,8,6},
            {4,2,1,11,10,13,7,8,15,9,12,5,6,3,0,14},
            {11,8,12,7,1,14,2,13,6,15,0,9,10,4,5,3}
	};

	int[][] s6 =
	{
            {12,1,10,15,9,2,6,8,0,13,3,4,14,7,5,11},
            {10,15,4,2,7,12,9,5,6,1,13,14,0,11,3,8},
            {9,14,15,5,2,8,12,3,7,0,4,10,1,13,11,6},
            {4,3,2,12,9,5,15,10,11,14,1,7,6,0,8,13}
	};

	int[][] s7 =
	{
            {4,11,2,14,15,0,8,13,3,12,9,7,5,10,6,1},
            {13,0,11,7,4,9,1,10,14,3,5,12,2,15,8,6},
            {1,4,11,13,12,3,7,14,10,15,6,8,0,5,9,2},
            {6,11,13,8,1,4,10,7,9,5,0,15,14,2,3,12}
	};

	int[][] s8 =
	{
            {13,2,8,4,6,15,11,1,10,9,3,14,5,0,12,7},
            {1,15,13,8,10,3,7,4,12,5,6,11,0,14,9,2},
            {7,11,4,1,9,12,14,2,0,6,10,13,15,3,5,8},
            {2,1,14,7,4,10,8,13,15,12,9,0,3,5,6,11}
	};
        
        // Table to get row #
	int[][] rT =
        {
            {0,1},
            {2,3}
	};
        
	// Table to get column #
	int[][] cT =
        {
            {0,1,2,3},
            {4,5,6,7},
            {8,9,10,11},
            {12,13,14,15}
	};

	int r, c; // rows and columns
	int[] tempER = new int[8];

	for (int i = 0; i < 8; i++) {
            r = rT[ER[0 + (i * 6)]][ER[5 + (i * 6)]]; // row number
            c = cT[rT[ER[1 + (i * 6)]][ER[2 + (i * 6)]]]
                  [rT[ER[3 + (i * 6)]][ER[4 + (i * 6)]]];
            
            switch (i) {
                case 0:
                    tempER[i] = s1[r][c];
                    break;
                case 1:
                    tempER[i] = s2[r][c];
                    break;
                case 2:
                    tempER[i] = s3[r][c];
                    break;
                case 3:
                    tempER[i] = s4[r][c];
                    break;
                case 4:
                    tempER[i] = s5[r][c];
                    break;
                case 5:
                    tempER[i] = s6[r][c];
                    break;
                case 6:
                    tempER[i] = s7[r][c];
                    break;
                case 7:
                    tempER[i] = s8[r][c];
                    break;
            }
	}
        
	int index = 0;
	for (int i = 0; i < 8; i++) {
            switch (tempER[i])
            {
                case 0:
                    for (int j = index; j < index + 4; j++) {
                        sER[j] = 0;
                    }
                    break;
                case 1:
                    for (int j = index; j < index + 4; j++) {
                        if (j == index + 3)
                            sER[j] = 1;
                        else
                            sER[j] = 0;
                    }
                    break;
                case 2:
                    for (int j = index; j < index + 4; j++) {
                        if (j == index + 2)
                            sER[j] = 1;
                        else
                            sER[j] = 0;
                    }
                    break;
                case 3:
                    for (int j = index; j < index + 4; j++) {
                        if (j > index + 1) 
                            sER[j] = 1;
                        else
                            sER[j] = 0;
                    }
                    break;
                case 4:
                    for (int j = index; j < index + 4; j++) {
                        if (j == index + 1)
                            sER[j] = 1;
                        else
                            sER[j] = 0;
                    }
                    break;
                case 5:
                    for (int j = index; j < index + 4; j++) {
                        if (j % 2 == 1)
                            sER[j] = 1;
                        else
                            sER[j] = 0;
                    }
                    break;
                case 6:
                    for (int j = index; j < index + 4; j++) {
                        if (j == index + 1 || j == index + 2)
                            sER[j] = 1;
                        else
                            sER[j] = 0;
                    }
                    break;
                case 7:
                    for (int j = index; j < index + 4; j++) {
                        if (j == index)
                            sER[j] = 0;
                        else
                            sER[j] = 1;
                    }
                    break;
                case 8:
                    for (int j = index; j < index + 4; j++) {
                        if (j == index)
                            sER[j] = 1;
                        else
                            sER[j] = 0;
                    }
                    break;
                case 9:
                    for (int j = index; j < index + 4; j++) {
                        if (j == index || j == index + 3)
                            sER[j] = 1;
                        else
                            sER[j] = 0;
                    }
                    break;
                case 10:
                    for (int j = index; j < index + 4; j++) {
                        if (j == index || j == index + 2)
                            sER[j] = 1;
                        else
                            sER[j] = 0;
                    }
                    break;
                case 11:
                    for (int j = index; j < index + 4; j++) {
                        if (j == index + 1)
                            sER[j] = 0;
                        else
                            sER[j] = 1;
                    }
                    break;
                case 12:
                    for (int j = index; j < index + 4; j++) {
                        if (j == index || j == index + 1)
                            sER[j] = 1;
                        else
                            sER[j] = 0;
                    }
                    break;
                case 13:
                    for (int j = index; j < index + 4; j++) {
                        if (j == index + 2)
                            sER[j] = 0;
                        else
                            sER[j] = 1;
                    }
                    break;
                case 14:
                    for (int j = index; j < index + 4; j++) {
                        if (j == index + 3)
                            sER[j] = 0;
                        else
                            sER[j] = 1;
                    }
                    break;
                case 15:
                    for (int j = index; j < index + 4; j++) {
                        sER[j] = 1;
                    }
                    break;
            }
            
            index = index + 4;
	}

	permute(sER);
	
        return sER;
    }
    
    void permutedInput(int K[], int L[], int R[]) {
	int i;
	int[] tempL = new int[32]; // Ln-1
	int[] ER = new int[48];
	int[] sER = new int[32];
        
	for (i = 0; i < 32; i++) {
            tempL[i] = L[i]; // Store left half in tempL
            L[i] = R[i]; // Ln = Rn-1
	}
        
	expand(R, ER); // Expand right half of message
	xorKE(K, ER); // XOR right half of message after expanding with key
	sub(ER, sER); // f(R,K)
	xorLF(tempL, sER); // XOR left half of message with round method
	
        // Setting R = L + f(R,K)
	for (i = 0; i < 32; i++)
            R[i] = sER[i];
    }
    
    int[] finalPerm(int m[]) {
	int[] tempM = new int[64];
	int[] fp =
	{
            40,8,48,16,56,24,64,32,
            39,7,47,15,55,23,63,31,
            38,6,46,14,54,22,62,30,
            37,5,45,13,53,21,61,29,
            36,4,44,12,52,20,60,28,
            35,3,43,11,51,19,59,27,
            34,2,42,10,50,18,58,26,
            33,1,41,9,49,17,57,25
	};
        
	for (int i = 0; i < 64; i++)
            tempM[i] = m[fp[i] - 1];
        
	for (int i = 0; i < 64; i++)
            m[i] = tempM[i];
        
	return m;
    }
    
    // Generate all 16 keys
    void keygen(int key[]) {
	PC1(key);
	int i;
	int[] C = new int[28]; // Ci
	int[] D = new int[28]; // Di
	int[] tempK = new int[56];
	int[] K = new int[48]; // subkey

	// Splitting key to C0 and D0
	for (i = 0; i < 28; i++)
            C[i] = key[i];
        
        // End splitting
	for (i = 0; i < 28; i++)
            D[i] = key[i + 28];
        
	for (int round = 1; round <= 16; round++) {
	    shiftL(C, D, round); // Call shifting method
            
            // Putting key back together
            for (i = 0; i < 28; i++)
                tempK[i] = C[i];

            for (i = 0; i < 28; i++)
                tempK[i + 28] = D[i];

            PC2(tempK); //pCompressing of 56 bit key to 48

            // End compressing to 48 bits
            for (i = 0; i < 48; i++)
                K[i] = tempK[i];
            
            storeKeys(K, round);
	}
    }
    
    void encrypt(int m[]) {
	int i;
	int[] L = new int[32]; // Ln
	int[] R = new int[32]; // Rn
	int[] tempK = new int[56];
	int[] K = new int[48]; // subkey

	// Split message into left and right halves
	for (i = 0; i < 32; i++)
            L[i] = m[i];
        
        // End splitting
	for (i = 0; i < 32; i++)
            R[i] = m[i + 32];
        
	IP(m, L, R); // Initial Permutation

	for (int round = 1; round <= 16; round++) {
            switch (round) {
                case 1:
                    for (i = 0; i < 48; i++) {
                        K[i] = k1[i];
                    }
                    break;
                case 2:
                    for (i = 0; i < 48; i++) {
                        K[i] = k2[i];
                    }
                    break;
                case 3:
                    for (i = 0; i < 48; i++) {
                        K[i] = k3[i];
                    }
                    break;
                case 4:
                    for (i = 0; i < 48; i++) {
                        K[i] = k4[i];
                    }
                    break;
                case 5:
                    for (i = 0; i < 48; i++) {
                        K[i] = k5[i];
                    }
                    break;
                case 6:
                    for (i = 0; i < 48; i++) {
                        K[i] = k6[i];
                    }
                    break;
                case 7:
                    for (i = 0; i < 48; i++) {
                        K[i] = k7[i];
                    }
                    break;
                case 8:
                    for (i = 0; i < 48; i++) {
                        K[i] = k8[i];
                    }
                    break;
                case 9:
                    for (i = 0; i < 48; i++) {
                        K[i] = k9[i];
                    }
                    break;
                case 10:
                    for (i = 0; i < 48; i++) {
                        K[i] = k10[i];
                    }
                    break;
                case 11:
                    for (i = 0; i < 48; i++) {
                        K[i] = k11[i];
                    }
                    break;
                case 12:
                    for (i = 0; i < 48; i++) {
                        K[i] = k12[i];
                    }
                    break;
                case 13:
                    for (i = 0; i < 48; i++) {
                        K[i] = k13[i];
                    }
                    break;
                case 14:
                    for (i = 0; i < 48; i++) {
                        K[i] = k14[i];
                    }
                    break;
                case 15:
                    for (i = 0; i < 48; i++) {
                        K[i] = k15[i];
                    }
                    break;
                case 16:
                    for (i = 0; i < 48; i++) {
                        K[i] = k16[i];
                    }
                    break;
            }
                   
            permutedInput(K, L, R);
	}

	// Reverse left and right halves
	for (i = 0; i < 32; i++)
		m[i] = R[i];

	for (i = 0; i < 32; i++)
		m[i + 32] = L[i];

	finalPerm(m);
    }
    
    void decrypt(int m[]) {
	int i;
	int[] L = new int[32]; // Ln
	int[] R = new int[32]; // Rn
	int[] tempK = new int[56];
	int[] K = new int[48]; // subkey

	// Split message into left and right halves
	for (i = 0; i < 32; i++)
            L[i] = m[i];
        
        // End splitting
	for (i = 0; i < 32; i++)
            R[i] = m[i + 32];
        
	IP(m, L, R); // Initial Permutation

	for (int round = 1; round <= 16; round++) {
	    switch (round) {
                case 1:
                    for (i = 0; i < 48; i++) {
                        K[i] = k16[i];
                    }
                    break;
                case 2:
                    for (i = 0; i < 48; i++) {
                        K[i] = k15[i];
                    }
                    break;
                case 3:
                    for (i = 0; i < 48; i++) {
                        K[i] = k14[i];
                    }
                    break;
                case 4:
                    for (i = 0; i < 48; i++) {
                        K[i] = k13[i];
                    }
                    break;
                case 5:
                    for (i = 0; i < 48; i++) {
                        K[i] = k12[i];
                    }
                    break;
                case 6:
                    for (i = 0; i < 48; i++) {
                        K[i] = k11[i];
                    }
                    break;
                case 7:
                    for (i = 0; i < 48; i++) {
                        K[i] = k10[i];
                    }
                    break;
                case 8:
                    for (i = 0; i < 48; i++) {
                        K[i] = k9[i];
                    }
                    break;
                case 9:
                    for (i = 0; i < 48; i++) {
                        K[i] = k8[i];
                    }
                    break;
                case 10:
                    for (i = 0; i < 48; i++) {
                        K[i] = k7[i];
                    }
                    break;
                case 11:
                    for (i = 0; i < 48; i++) {
                        K[i] = k6[i];
                    }
                    break;
                case 12:
                    for (i = 0; i < 48; i++) {
                        K[i] = k5[i];
                    }
                    break;
                case 13:
                    for (i = 0; i < 48; i++) {
                        K[i] = k4[i];
                    }
                    break;
                case 14:
                    for (i = 0; i < 48; i++) {
                        K[i] = k3[i];
                    }
                    break;
                case 15:
                    for (i = 0; i < 48; i++) {
                        K[i] = k2[i];
                    }
                    break;
                case 16:
                    for (i = 0; i < 48; i++) {
                        K[i] = k1[i];
                    }
                    break;
            }

            permutedInput(K, L, R);
	}

	 // Reverse left and right halves
	for (i = 0; i < 32; i++)
		m[i] = R[i];

	for (i = 0; i < 32; i++)
		m[i + 32] = L[i];

	finalPerm(m);
    }
    
    void des(int key[], int m[]) {
	keygen(key); // Generate keys
	encrypt(m); // Encrypt message

	System.out.println("Encrypted Message =");
	for (int i = 0; i < 64; i++) {
	    if (i % 4 == 0)
                System.out.print(" ");

            System.out.print(m[i]);
	}

	decrypt(m); // Decrypt message
	
	System.out.println("\nDecrypted Message =");
	for (int i = 0; i < 64; i++) {
            if (i % 4 == 0)
                System.out.print(" ");

            System.out.print(m[i]);
	}
    }
}