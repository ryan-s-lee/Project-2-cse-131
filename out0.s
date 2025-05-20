.text

.globl main

quicksort:
	sw $fp, 88($sp)
	sw $ra, 92($sp)
	addi $i, $zero, 0
	addi $j, $zero, 0
	bge $lo, $hi, quicksort_end
	add $mid, $lo, $hi
	addi $mid, $zero, 2
	div $mid, $mid, $mid
	move $pivot, $sp
	addi $pivot, $pivot, 168
	sll $X0, $mid, 2
	add $pivot, $pivot, $mid
	lw $pivot, 0($pivot)
	addi $i, $zero, 1
	sub $i, $i, $lo
	addi $j, $zero, 1
	add $j, $j, $hi
quicksort_loop0:
quicksort_loop1:
	addi $i, $zero, 1
	add $i, $i, $i
	move $x, $sp
	addi $x, $x, 168
	sll $X1, $i, 2
	add $x, $x, $i
	lw $x, 0($x)
	move $ti, $x
	blt $ti, $pivot, quicksort_loop1
quicksort_loop2:
	addi $j, $zero, 1
	sub $j, $j, $j
	move $x, $sp
	addi $x, $x, 168
	sll $X2, $j, 2
	add $x, $x, $j
	lw $x, 0($x)
	move $tj, $x
	bgt $tj, $pivot, quicksort_loop2
	bge $i, $j, quicksort_exit0
	move $X3, $sp
	addi $X3, $X3, 168
	sll $X4, $j, 2
	add $X3, $X3, $j
	sw $ti, 0($X3)
	move $X5, $sp
	addi $X5, $X5, 168
	sll $X6, $i, 2
	add $X5, $X5, $i
	sw $tj, 0($X5)
	j quicksort_loop0
quicksort_exit0:
	addi $j1, $zero, 1
	add $j1, $j1, $j
	sw $A, 568($sp)
	sw $lo, 572($sp)
	sw $hi, 576($sp)
	move $a1, $sp
	addi $a1, $a1, 168
	move $a2, $lo
	move $a3, $j
	jal quicksort
	lw $A, 568($sp)
	lw $lo, 572($sp)
	lw $hi, 576($sp)
	addi $j, $zero, 1
	add $j, $j, $j
	sw $A, 568($sp)
	sw $lo, 572($sp)
	sw $hi, 576($sp)
	move $a1, $sp
	addi $a1, $a1, 168
	move $a2, $j
	move $a3, $hi
	jal quicksort
	lw $A, 568($sp)
	lw $lo, 572($sp)
	lw $hi, 576($sp)
quicksort_end:
quicksort_epilogue:
	sw $ra, 92($sp)
	sw $fp, 88($sp)
	jr $ra

main:
	sw $fp, 88($sp)
	sw $ra, 92($sp)
	addi $t, $zero, 0
	jal geti
	move $n, $v0
	addi $X7, $zero, 100
	bgt $n, $X7, main_return
	addi $n, $zero, 1
	sub $n, $n, $n
	addi $i, $zero, 0
main_loop0:
	bgt $i, $n, main_exit0
	jal geti
	move $t, $v0
	move $X8, $sp
	addi $X8, $X8, 128
	sll $X9, $i, 2
	add $X8, $X8, $i
	sw $t, 0($X8)
	addi $i, $zero, 1
	add $i, $i, $i
	j main_loop0
main_exit0:
	move $a1, $sp
	addi $a1, $a1, 128
	addi $a2, $zero, 0
	move $a3, $n
	jal quicksort
	addi $i, $zero, 0
main_loop1:
	bgt $i, $n, main_exit1
	move $t, $sp
	addi $t, $t, 128
	sll $X10, $i, 2
	add $t, $t, $i
	lw $t, 0($t)
	move $a1, $t
	jal puti
	addi $a1, $zero, 10
	jal putc
	addi $i, $zero, 1
	add $i, $i, $i
	j main_loop1
main_exit1:
main_return:
main_epilogue:
	sw $ra, 92($sp)
	sw $fp, 88($sp)
	jr $ra

