.globl main

.text

main:
	addi $sp, $sp, -184
	addi $t0, $zero, 0
	sw $t0, 164($sp)
	addi $t0, $zero, 2
	sw $t0, 176($sp)
	addi $t0, $zero, 3
	sw $t0, 172($sp)
	addi $t0, $zero, 6
	sw $t0, 168($sp)
	addi $t0, $zero, 0
	sw $t0, 132($sp)
	addi $v0, $zero, 5
	syscall
	move $t0, $v0
	sw $t0, 160($sp)
	addi $t0, $zero, 1
	sw $t0, 124($sp)
	lw $t1, 160($sp)
	lw $t2, 124($sp)
	bgt $t1, $t2, main_label0
	addi $t0, $zero, 0
	sw $t0, 156($sp)
	lw $t1, 156($sp)
	move $t0, $t1
	sw $t0, 180($sp)
	j main_print
main_label0:
	addi $t0, $zero, 3
	sw $t0, 120($sp)
	lw $t1, 160($sp)
	lw $t2, 120($sp)
	bgt $t1, $t2, main_label1
	addi $t0, $zero, 1
	sw $t0, 156($sp)
	lw $t1, 156($sp)
	move $t0, $t1
	sw $t0, 180($sp)
	j main_print
main_label1:
	lw $t1, 160($sp)
	move $a0, $t1
	lw $t1, 176($sp)
	move $a1, $t1
	jal divisible
	move $t0, $v0
	sw $t0, 148($sp)
	lw $t1, 132($sp)
	move $t0, $t1
	sw $t0, 156($sp)
	lw $t1, 156($sp)
	move $t0, $t1
	sw $t0, 180($sp)
	addi $t0, $zero, 1
	sw $t0, 116($sp)
	lw $t1, 148($sp)
	lw $t2, 116($sp)
	beq $t1, $t2, main_label2
	lw $t1, 160($sp)
	move $a0, $t1
	lw $t1, 172($sp)
	move $a1, $t1
	jal divisible
	move $t0, $v0
	sw $t0, 148($sp)
	lw $t1, 132($sp)
	move $t0, $t1
	sw $t0, 156($sp)
	lw $t1, 156($sp)
	move $t0, $t1
	sw $t0, 180($sp)
	addi $t0, $zero, 1
	sw $t0, 112($sp)
	lw $t1, 148($sp)
	lw $t2, 112($sp)
	beq $t1, $t2, main_label2
	j main_label3
main_label2:
	j main_print
main_label3:
	addi $t0, $zero, 5
	sw $t0, 164($sp)
main_loop:
	lw $t1, 164($sp)
	lw $t2, 164($sp)
	mul $t0, $t1, $t2
	sw $t0, 152($sp)
	lw $t1, 152($sp)
	lw $t2, 160($sp)
	bgt $t1, $t2, main_exit
	lw $t1, 160($sp)
	move $a0, $t1
	lw $t1, 164($sp)
	move $a1, $t1
	jal divisible
	move $t0, $v0
	sw $t0, 148($sp)
	lw $t1, 132($sp)
	move $t0, $t1
	sw $t0, 156($sp)
	addi $t0, $zero, 0
	sw $t0, 140($sp)
	addi $t0, $zero, 0
	sw $t0, 128($sp)
	lw $t1, 156($sp)
	move $t0, $t1
	sw $t0, 180($sp)
	addi $t0, $zero, 1
	sw $t0, 108($sp)
	lw $t1, 148($sp)
	lw $t2, 108($sp)
	beq $t1, $t2, main_label2
	addi $t0, $zero, 2
	sw $t0, 104($sp)
	lw $t1, 164($sp)
	lw $t2, 104($sp)
	add $t0, $t1, $t2
	sw $t0, 144($sp)
	lw $t1, 160($sp)
	move $a0, $t1
	lw $t1, 144($sp)
	move $a1, $t1
	jal divisible
	move $t0, $v0
	sw $t0, 148($sp)
	lw $t1, 132($sp)
	move $t0, $t1
	sw $t0, 156($sp)
	lw $t1, 156($sp)
	move $t0, $t1
	sw $t0, 180($sp)
	addi $t0, $zero, 1
	sw $t0, 100($sp)
	lw $t1, 148($sp)
	lw $t2, 100($sp)
	beq $t1, $t2, main_label2
	addi $t0, $zero, 6
	sw $t0, 96($sp)
	lw $t1, 164($sp)
	lw $t2, 96($sp)
	add $t0, $t1, $t2
	sw $t0, 164($sp)
	j main_loop
main_exit:
	lw $t1, 140($sp)
	move $t0, $t1
	sw $t0, 136($sp)
	lw $t1, 128($sp)
	move $t0, $t1
	sw $t0, 156($sp)
	addi $t0, $zero, 1
	sw $t0, 156($sp)
	lw $t1, 156($sp)
	move $t0, $t1
	sw $t0, 180($sp)
main_print:
	lw $t1, 180($sp)
	move $a0, $t1
	addi $v0, $zero, 1
	syscall
	addi $a0, $zero, 10
	addi $v0, $zero, 11
	syscall
main_epilogue:
	addi $sp, $sp, 184
	li $v0, 10
	syscall

divisible:
	addi $sp, $sp, -104
	div $t0, $a0, $a1
	sw $t0, 100($sp)
	lw $t1, 100($sp)
	mul $t0, $t1, $a1
	sw $t0, 100($sp)
	lw $t2, 100($sp)
	bne $a0, $t2, divisible_label0
	addi $v0, $zero, 1
	j divisible_epilogue
divisible_label0:
	addi $v0, $zero, 0
	j divisible_epilogue
divisible_epilogue:
	addi $sp, $sp, 104
	jr $ra

