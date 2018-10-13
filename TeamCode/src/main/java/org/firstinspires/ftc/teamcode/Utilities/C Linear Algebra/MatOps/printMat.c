#include "printMat.h"
#include <stdio.h>
//Hello
void iPrintMat(int *mat, int sizeRow, int sizeCol) {
	//Print 2d array of integers

	int i,j;

	for(i=0; i<sizeRow;i++) {
		for(j=0;j<sizeCol;j++) {
			printf(" %d",*(mat+(i*sizeRow)+j)); // Dereference pointer to wanted address
		}
		printf("\n");
	}
}
void fPrintMat(float *mat, int sizeRow, int sizeCol) {
	//Print 2d array of floats
	int i,j;

	for(i=0; i<sizeRow;i++) {
		for(j=0;j<sizeCol;j++) {
			printf(" %f",*(mat+(i*sizeRow)+j)); // Dereference pointer to wanted address
		}
		printf("\n");
	}
}

void dPrintMat(double *mat, int sizeRow, int sizeCol) {
	//Print 2d array of doubles
	int i,j;

	for(i=0; i<sizeRow;i++) {
		for(j=0;j<sizeCol;j++) {
			printf(" %lf",*(mat+(i*sizeRow)+j)); // Dereference pointer to wanted address
		}
		printf("\n");
	}
}
