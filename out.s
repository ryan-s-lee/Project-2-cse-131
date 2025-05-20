quicksort:
	sw $fp, 88($sp)
	sw $ra, 92($sp)
	addi $t0, $zero, 0
	sw $t0, 132($sp)
	addi $t0, $zero, 0
	sw $t0, 128($sp)
	lw $t1, 160($sp)
	lw $t2, 164($sp)
	bge $t0, $t1, quicksort_end
	lw $t1, 160($sp)
	lw $t2, 164($sp)
	add $t0, $t1, $t2
	sw $t0, 140($sp)
	addi $t0, $zero, 2
	sw $t0, 140($sp)
	lw $t1, 140($sp)
	lw $t2, 140($sp)
	div $t0, $t1, $t2
	sw $t0, 140($sp)
	move $t0, $sp
	sw $t0, 136($sp)
	lw $t1, 136($sp)
	addi $t0, $t1, 168
	sw $t0, 136($sp)
	lw $t1, 140($sp)
	sll $t0, $t1, 2
	sw $t0, 124($sp)
	lw $t1, 136($sp)
	lw $t2, 140($sp)
	add $t0, $t1, $t2
	sw $t0, 136($sp)
	lw $t1, 136($sp)
	lw $t0, 0($t1)
	sw $t0, 136($sp)
	addi $t0, $zero, 1
	sw $t0, 132($sp)
	lw $t1, 132($sp)
	lw $t2, 160($sp)
	sub $t0, $t1, $t2
	sw $t0, 132($sp)
	addi $t0, $zero, 1
	sw $t0, 128($sp)
	lw $t1, 128($sp)
	lw $t2, 164($sp)
	add $t0, $t1, $t2
	sw $t0, 128($sp)
quicksort_loop0:
quicksort_loop1:
	addi $t0, $zero, 1
	sw $t0, 132($sp)
	lw $t1, 132($sp)
	lw $t2, 132($sp)
	add $t0, $t1, $t2
	sw $t0, 132($sp)
	move $t0, $sp
	sw $t0, 144($sp)
	lw $t1, 144($sp)
	addi $t0, $t1, 168
	sw $t0, 144($sp)
	lw $t1, 132($sp)
	sll $t0, $t1, 2
	sw $t0, 120($sp)
	lw $t1, 144($sp)
	lw $t2, 132($sp)
	add $t0, $t1, $t2
	sw $t0, 144($sp)
	lw $t1, 144($sp)
	lw $t0, 0($t1)
	sw $t0, 144($sp)
	lw $t1, 144($sp)
	move $t0, $t1
	sw $t0, 156($sp)
	lw $t1, 156($sp)
	lw $t2, 136($sp)
	blt $t0, $t1, quicksort_loop1
quicksort_loop2:
	addi $t0, $zero, 1
	sw $t0, 128($sp)
	lw $t1, 128($sp)
	lw $t2, 128($sp)
	sub $t0, $t1, $t2
	sw $t0, 128($sp)
	move $t0, $sp
	sw $t0, 144($sp)
	lw $t1, 144($sp)
	addi $t0, $t1, 168
	sw $t0, 144($sp)
	lw $t1, 128($sp)
	sll $t0, $t1, 2
	sw $t0, 116($sp)
	lw $t1, 144($sp)
	lw $t2, 128($sp)
	add $t0, $t1, $t2
	sw $t0, 144($sp)
	lw $t1, 144($sp)
	lw $t0, 0($t1)
	sw $t0, 144($sp)
	lw $t1, 144($sp)
	move $t0, $t1
	sw $t0, 152($sp)
	lw $t1, 152($sp)
	lw $t2, 136($sp)
	bgt $t0, $t1, quicksort_loop2
	lw $t1, 132($sp)
	lw $t2, 128($sp)
	bge $t0, $t1, quicksort_exit0
	move $t0, $sp
	sw $t0, 112($sp)
	lw $t1, 112($sp)
	addi $t0, $t1, 168
	sw $t0, 112($sp)
	lw $t1, 128($sp)
	sll $t0, $t1, 2
	sw $t0, 108($sp)
	lw $t1, 112($sp)
	lw $t2, 128($sp)
	add $t0, $t1, $t2
	sw $t0, 112($sp)
	lw $t1, 156($sp)
	lw $t2, 112($sp)
	sw $t0, 0($t1)
	move $t0, $sp
	sw $t0, 104($sp)
	lw $t1, 104($sp)
	addi $t0, $t1, 168
	sw $t0, 104($sp)
	lw $t1, 132($sp)
	sll $t0, $t1, 2
	sw $t0, 100($sp)
	lw $t1, 104($sp)
	lw $t2, 132($sp)
	add $t0, $t1, $t2
	sw $t0, 104($sp)
	lw $t1, 152($sp)
	lw $t2, 104($sp)
	sw $t0, 0($t1)
	j quicksort_loop0
quicksort_exit0:
	addi $t0, $zero, 1
	sw $t0, 148($sp)
	lw $t1, 148($sp)
	lw $t2, 128($sp)
	add $t0, $t1, $t2
	sw $t0, 148($sp)
	lw $t1, 568($sp)
	sw $t0, 568($sp)
	lw $t1, 572($sp)
	sw $t0, 572($sp)
	lw $t1, 576($sp)
	sw $t0, 576($sp)
	move $a1, $sp
	addi $a1, $a1, 168
	lw $t1, 160($sp)
	move $a2, $t1
	lw $t1, 128($sp)
	move $a3, $t1
	jal quicksort
	lw $t1, 568($sp)
	lw $t0, 568($sp)
	sw $t0, 568($sp)
	lw $t1, 572($sp)
	lw $t0, 572($sp)
	sw $t0, 572($sp)
	lw $t1, 576($sp)
	lw $t0, 576($sp)
	sw $t0, 576($sp)
	addi $t0, $zero, 1
	sw $t0, 128($sp)
	lw $t1, 128($sp)
	lw $t2, 128($sp)
	add $t0, $t1, $t2
	sw $t0, 128($sp)
	lw $t1, 568($sp)
	sw $t0, 568($sp)
	lw $t1, 572($sp)
	sw $t0, 572($sp)
	lw $t1, 576($sp)
	sw $t0, 576($sp)
	move $a1, $sp
	addi $a1, $a1, 168
	lw $t1, 128($sp)
	move $a2, $t1
	lw $t1, 164($sp)
	move $a3, $t1
	jal quicksort
	lw $t1, 568($sp)
	lw $t0, 568($sp)
	sw $t0, 568($sp)
	lw $t1, 572($sp)
	lw $t0, 572($sp)
	sw $t0, 572($sp)
	lw $t1, 576($sp)
	lw $t0, 576($sp)
	sw $t0, 576($sp)
quicksort_end:
quicksort_epilogue:
	sw $ra, 92($sp)
	sw $fp, 88($sp)
	jr $ra

main:
	sw $fp, 88($sp)
	sw $ra, 92($sp)
	addi $t0, $zero, 0
	sw $t0, 124($sp)
	addi $v0, $zero, 5
	syscall
	move $t0, $v0
	sw $t0, 116($sp)
	addi $t0, $zero, 100
	sw $t0, 112($sp)
	lw $t1, 116($sp)
	lw $t2, 112($sp)
	bgt $t0, $t1, main_return
	addi $t0, $zero, 1
	sw $t0, 116($sp)
	lw $t1, 116($sp)
	lw $t2, 116($sp)
	sub $t0, $t1, $t2
	sw $t0, 116($sp)
	addi $t0, $zero, 0
	sw $t0, 120($sp)
main_loop0:
	lw $t1, 120($sp)
	lw $t2, 116($sp)
	bgt $t0, $t1, main_exit0
	addi $v0, $zero, 5
	syscall
	move $t0, $v0
	sw $t0, 124($sp)
	move $t0, $sp
	sw $t0, 108($sp)
	lw $t1, 108($sp)
	addi $t0, $t1, 128
	sw $t0, 108($sp)
	lw $t1, 120($sp)
	sll $t0, $t1, 2
	sw $t0, 104($sp)
	lw $t1, 108($sp)
	lw $t2, 120($sp)
	add $t0, $t1, $t2
	sw $t0, 108($sp)
	lw $t1, 124($sp)
	lw $t2, 108($sp)
	sw $t0, 0($t1)
	addi $t0, $zero, 1
	sw $t0, 120($sp)
	lw $t1, 120($sp)
	lw $t2, 120($sp)
	add $t0, $t1, $t2
	sw $t0, 120($sp)
	j main_loop0
main_exit0:
	move $a1, $sp
	addi $a1, $a1, 128
	addi $a2, $zero, 0
	lw $t1, 116($sp)
	move $a3, $t1
	jal quicksort
	addi $t0, $zero, 0
	sw $t0, 120($sp)
main_loop1:
	lw $t1, 120($sp)
	lw $t2, 116($sp)
	bgt $t0, $t1, main_exit1
	move $t0, $sp
	sw $t0, 124($sp)
	lw $t1, 124($sp)
	addi $t0, $t1, 128
	sw $t0, 124($sp)
	lw $t1, 120($sp)
	sll $t0, $t1, 2
	sw $t0, 100($sp)
	lw $t1, 124($sp)
	lw $t2, 120($sp)
	add $t0, $t1, $t2
	sw $t0, 124($sp)
	lw $t1, 124($sp)
	lw $t0, 0($t1)
	sw $t0, 124($sp)
	lw $t1, 124($sp)
	move $a1, $t1
	addi $v0, $zero, 1
	syscall
	addi $a1, $zero, 10
	addi $v0, $zero, 11
	syscall
	addi $t0, $zero, 1
	sw $t0, 120($sp)
	lw $t1, 120($sp)
	lw $t2, 120($sp)
	add $t0, $t1, $t2
	sw $t0, 120($sp)
	j main_loop1
main_exit1:
main_return:
main_epilogue:
	sw $ra, 92($sp)
	sw $fp, 88($sp)
	jr $ra

