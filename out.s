.globl main

.text

main:
	addi $sp, $sp, -544
	addi $t0, $zero, 0
	sw $t0, 132($sp)
	addi $v0, $zero, 5
	syscall
	move $t0, $v0
	sw $t0, 124($sp)
	addi $t0, $zero, 100
	sw $t0, 120($sp)
	lw $t1, 124($sp)
	lw $t2, 120($sp)
	bgt $t1, $t2, main_return
	addi $t0, $zero, 1
	sw $t0, 116($sp)
	lw $t1, 124($sp)
	lw $t2, 116($sp)
	sub $t0, $t1, $t2
	sw $t0, 124($sp)
	addi $t0, $zero, 0
	sw $t0, 128($sp)
main_loop0:
	lw $t1, 128($sp)
	lw $t2, 124($sp)
	bgt $t1, $t2, main_exit0
	addi $v0, $zero, 5
	syscall
	move $t0, $v0
	sw $t0, 132($sp)
	move $t0, $sp
	sw $t0, 112($sp)
	lw $t1, 112($sp)
	addi $t0, $t1, 136
	sw $t0, 112($sp)
	lw $t1, 128($sp)
	sll $t0, $t1, 2
	sw $t0, 108($sp)
	lw $t1, 112($sp)
	lw $t2, 108($sp)
	add $t0, $t1, $t2
	sw $t0, 112($sp)
	lw $t1, 132($sp)
	lw $t2, 112($sp)
	sw $t1, 0($t2)
	addi $t0, $zero, 1
	sw $t0, 104($sp)
	lw $t1, 128($sp)
	lw $t2, 104($sp)
	add $t0, $t1, $t2
	sw $t0, 128($sp)
	j main_loop0
main_exit0:
	move $a0, $sp
	addi $a0, $a0, 136
	addi $a1, $zero, 0
	lw $t1, 124($sp)
	move $a2, $t1
	jal quicksort
	addi $t0, $zero, 0
	sw $t0, 128($sp)
main_loop1:
	lw $t1, 128($sp)
	lw $t2, 124($sp)
	bgt $t1, $t2, main_exit1
	move $t0, $sp
	sw $t0, 132($sp)
	lw $t1, 132($sp)
	addi $t0, $t1, 136
	sw $t0, 132($sp)
	lw $t1, 128($sp)
	sll $t0, $t1, 2
	sw $t0, 100($sp)
	lw $t1, 132($sp)
	lw $t2, 100($sp)
	add $t0, $t1, $t2
	sw $t0, 132($sp)
	lw $t1, 132($sp)
	lw $t0, 0($t1)
	sw $t0, 132($sp)
	lw $t1, 132($sp)
	move $a0, $t1
	addi $v0, $zero, 1
	syscall
	addi $a0, $zero, 10
	addi $v0, $zero, 11
	syscall
	addi $t0, $zero, 1
	sw $t0, 96($sp)
	lw $t1, 128($sp)
	lw $t2, 96($sp)
	add $t0, $t1, $t2
	sw $t0, 128($sp)
	j main_loop1
main_exit1:
main_return:
main_epilogue:
	addi $sp, $sp, 544
	li $v0, 10
	syscall

quicksort:
	addi $sp, $sp, -176
	sw $fp, 88($sp)
	sw $ra, 92($sp)
	addi $t0, $zero, 0
	sw $t0, 140($sp)
	addi $t0, $zero, 0
	sw $t0, 136($sp)
	bge $a1, $a2, quicksort_end
	add $t0, $a1, $a2
	sw $t0, 148($sp)
	addi $t0, $zero, 2
	sw $t0, 132($sp)
	lw $t1, 148($sp)
	lw $t2, 132($sp)
	div $t0, $t1, $t2
	sw $t0, 148($sp)
	lw $t1, 148($sp)
	sll $t0, $t1, 2
	sw $t0, 144($sp)
	lw $t1, 144($sp)
	add $t0, $t1, $a0
	sw $t0, 144($sp)
	lw $t1, 144($sp)
	lw $t0, 0($t1)
	sw $t0, 144($sp)
	addi $t0, $zero, 1
	sw $t0, 128($sp)
	lw $t2, 128($sp)
	sub $t0, $a1, $t2
	sw $t0, 140($sp)
	addi $t0, $zero, 1
	sw $t0, 124($sp)
	lw $t2, 124($sp)
	add $t0, $a2, $t2
	sw $t0, 136($sp)
quicksort_loop0:
quicksort_loop1:
	addi $t0, $zero, 1
	sw $t0, 120($sp)
	lw $t1, 140($sp)
	lw $t2, 120($sp)
	add $t0, $t1, $t2
	sw $t0, 140($sp)
	lw $t1, 140($sp)
	sll $t0, $t1, 2
	sw $t0, 152($sp)
	lw $t1, 152($sp)
	add $t0, $t1, $a0
	sw $t0, 152($sp)
	lw $t1, 152($sp)
	lw $t0, 0($t1)
	sw $t0, 152($sp)
	lw $t1, 152($sp)
	move $t0, $t1
	sw $t0, 164($sp)
	lw $t1, 164($sp)
	lw $t2, 144($sp)
	blt $t1, $t2, quicksort_loop1
quicksort_loop2:
	addi $t0, $zero, 1
	sw $t0, 116($sp)
	lw $t1, 136($sp)
	lw $t2, 116($sp)
	sub $t0, $t1, $t2
	sw $t0, 136($sp)
	lw $t1, 136($sp)
	sll $t0, $t1, 2
	sw $t0, 152($sp)
	lw $t1, 152($sp)
	add $t0, $t1, $a0
	sw $t0, 152($sp)
	lw $t1, 152($sp)
	lw $t0, 0($t1)
	sw $t0, 152($sp)
	lw $t1, 152($sp)
	move $t0, $t1
	sw $t0, 160($sp)
	lw $t1, 160($sp)
	lw $t2, 144($sp)
	bgt $t1, $t2, quicksort_loop2
	lw $t1, 140($sp)
	lw $t2, 136($sp)
	bge $t1, $t2, quicksort_exit0
	lw $t1, 136($sp)
	sll $t0, $t1, 2
	sw $t0, 112($sp)
	lw $t1, 112($sp)
	add $t0, $t1, $a0
	sw $t0, 112($sp)
	lw $t1, 164($sp)
	lw $t2, 112($sp)
	sw $t1, 0($t2)
	lw $t1, 140($sp)
	sll $t0, $t1, 2
	sw $t0, 108($sp)
	lw $t1, 108($sp)
	add $t0, $t1, $a0
	sw $t0, 108($sp)
	lw $t1, 160($sp)
	lw $t2, 108($sp)
	sw $t1, 0($t2)
	j quicksort_loop0
quicksort_exit0:
	addi $t0, $zero, 1
	sw $t0, 104($sp)
	lw $t1, 136($sp)
	lw $t2, 104($sp)
	add $t0, $t1, $t2
	sw $t0, 156($sp)
	sw $a0, 176($sp)
	sw $a1, 180($sp)
	sw $a2, 184($sp)
	move $a0, $a0
	move $a1, $a1
	lw $t1, 136($sp)
	move $a2, $t1
	jal quicksort
	lw $a0, 176($sp)
	lw $a1, 180($sp)
	lw $a2, 184($sp)
	addi $t0, $zero, 1
	sw $t0, 100($sp)
	lw $t1, 136($sp)
	lw $t2, 100($sp)
	add $t0, $t1, $t2
	sw $t0, 136($sp)
	sw $a0, 176($sp)
	sw $a1, 180($sp)
	sw $a2, 184($sp)
	move $a0, $a0
	lw $t1, 136($sp)
	move $a1, $t1
	move $a2, $a2
	jal quicksort
	lw $a0, 176($sp)
	lw $a1, 180($sp)
	lw $a2, 184($sp)
quicksort_end:
quicksort_epilogue:
	lw $ra, 92($sp)
	lw $fp, 88($sp)
	addi $sp, $sp, 176
	jr $ra

