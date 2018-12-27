(module 
  (import "system" "printInt" (func $Std_printInt (param i32) (result i32)))
  (import "system" "printString" (func $Std_printString (param i32) (result i32)))
  (import "system" "readString0" (func $js_readString0 (param i32) (result i32)))
  (import "system" "readInt" (func $Std_readInt (result i32)))
  (import "system" "mem" (memory 100))
  (global (mut i32) i32.const 0) 
  (table (export "lookup") 22 anyfunc)
  (elem (i32.const 0) 
    $O_isDefined 
    $O_get 
    $O_getOrElse 
    $O_orElse 
    $O_toList 
    $L_isEmpty 
    $L_length 
    $L_head 
    $L_headOption 
    $L_reverse 
    $L_reverseAcc 
    $L_indexOf 
    $L_range 
    $L_sum 
    $L_concat 
    $L_contains 
    $L_merge 
    $L_split 
    $L_mergeSort 
    $L_toString 
    $L_toString1 
    $L_take 
  )
  (type $return_iti (func (param i32) (result i32)))

  (func $String_concat (param i32 i32) (result i32) (local i32 i32)
    get_global 0
    set_local 3
    get_local 0
    set_local 2
    loop $label_1
      get_local 2
      i32.load8_u
      if
        get_local 3
        get_local 2
        i32.load8_u
        i32.store8
        get_local 3
        i32.const 1
        i32.add
        set_local 3
        get_local 2
        i32.const 1
        i32.add
        set_local 2
        br $label_1
      else
      end
    end
    get_local 1
    set_local 2
    loop $label_2
      get_local 2
      i32.load8_u
      if
        get_local 3
        get_local 2
        i32.load8_u
        i32.store8
        get_local 3
        i32.const 1
        i32.add
        set_local 3
        get_local 2
        i32.const 1
        i32.add
        set_local 2
        br $label_2
      else
      end
    end
    loop $label_0
      get_local 3
      i32.const 0
      i32.store8
      get_local 3
      i32.const 4
      i32.rem_s
      if
        get_local 3
        i32.const 1
        i32.add
        set_local 3
        br $label_0
      else
      end
    end
    get_global 0
    get_local 3
    i32.const 1
    i32.add
    set_global 0
  )

  (func $Std_digitToString (param i32) (result i32) 
    get_global 0
    get_local 0
    i32.const 48
    i32.add
    i32.store
    get_global 0
    get_global 0
    i32.const 4
    i32.add
    set_global 0
  )

  (func $Std_readString (result i32) 
    get_global 0
    get_global 0
    call $js_readString0
    set_global 0
  )

  (func $Std_printBoolean (param i32) (result i32) 
    get_local 0
    call $Std_booleanToString
    call $Std_printString
  )

  (func $Std_intToString (param i32) (result i32) (local i32 i32)
    get_local 0
    i32.const 0
    i32.lt_s
    if (result i32)
      get_global 0
      i32.const 0
      i32.add
      i32.const 45
      i32.store8
      get_global 0
      i32.const 1
      i32.add
      i32.const 0
      i32.store8
      get_global 0
      i32.const 2
      i32.add
      i32.const 0
      i32.store8
      get_global 0
      i32.const 3
      i32.add
      i32.const 0
      i32.store8
      get_global 0
      get_global 0
      i32.const 4
      i32.add
      set_global 0
      i32.const 0
      get_local 0
      i32.sub
      call $Std_intToString
      call $String_concat
    else
      get_local 0
      i32.const 10
      i32.rem_s
      set_local 1
      get_local 0
      i32.const 10
      i32.div_s
      set_local 2
      get_local 2
      i32.const 0
      i32.eq
      if (result i32)
        get_local 1
        call $Std_digitToString
      else
        get_local 2
        call $Std_intToString
        get_local 1
        call $Std_digitToString
        call $String_concat
      end
    end
  )

  (func $Std_booleanToString (param i32) (result i32) 
    get_local 0
    if (result i32)
      get_global 0
      i32.const 0
      i32.add
      i32.const 116
      i32.store8
      get_global 0
      i32.const 1
      i32.add
      i32.const 114
      i32.store8
      get_global 0
      i32.const 2
      i32.add
      i32.const 117
      i32.store8
      get_global 0
      i32.const 3
      i32.add
      i32.const 101
      i32.store8
      get_global 0
      i32.const 4
      i32.add
      i32.const 0
      i32.store8
      get_global 0
      i32.const 5
      i32.add
      i32.const 0
      i32.store8
      get_global 0
      i32.const 6
      i32.add
      i32.const 0
      i32.store8
      get_global 0
      i32.const 7
      i32.add
      i32.const 0
      i32.store8
      get_global 0
      get_global 0
      i32.const 8
      i32.add
      set_global 0
    else
      get_global 0
      i32.const 0
      i32.add
      i32.const 102
      i32.store8
      get_global 0
      i32.const 1
      i32.add
      i32.const 97
      i32.store8
      get_global 0
      i32.const 2
      i32.add
      i32.const 108
      i32.store8
      get_global 0
      i32.const 3
      i32.add
      i32.const 115
      i32.store8
      get_global 0
      i32.const 4
      i32.add
      i32.const 101
      i32.store8
      get_global 0
      i32.const 5
      i32.add
      i32.const 0
      i32.store8
      get_global 0
      i32.const 6
      i32.add
      i32.const 0
      i32.store8
      get_global 0
      i32.const 7
      i32.add
      i32.const 0
      i32.store8
      get_global 0
      get_global 0
      i32.const 8
      i32.add
      set_global 0
    end
  )

  (func $O_isDefined (param i32) (result i32) (local i32)
    get_local 0
    set_local 1
    get_local 1
    i32.load
    i32.const 0
    i32.eq
    if (result i32)
      i32.const 0
    else
      i32.const 1
      if (result i32)
        i32.const 1
      else
        get_global 0
        i32.const 0
        i32.add
        i32.const 77
        i32.store8
        get_global 0
        i32.const 1
        i32.add
        i32.const 97
        i32.store8
        get_global 0
        i32.const 2
        i32.add
        i32.const 116
        i32.store8
        get_global 0
        i32.const 3
        i32.add
        i32.const 99
        i32.store8
        get_global 0
        i32.const 4
        i32.add
        i32.const 104
        i32.store8
        get_global 0
        i32.const 5
        i32.add
        i32.const 32
        i32.store8
        get_global 0
        i32.const 6
        i32.add
        i32.const 101
        i32.store8
        get_global 0
        i32.const 7
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 8
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 9
        i32.add
        i32.const 111
        i32.store8
        get_global 0
        i32.const 10
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 11
        i32.add
        i32.const 0
        i32.store8
        get_global 0
        get_global 0
        i32.const 12
        i32.add
        set_global 0
        call $Std_printString
        unreachable
      end
    end
  )

  (func $O_get (param i32) (result i32) (local i32 i32 i32)
    get_local 0
    set_local 1
    get_local 1
    i32.load
    i32.const 1
    i32.eq
    get_local 1
    i32.const 4
    i32.add
    i32.load
    set_local 2
    get_local 2
    set_local 3
    i32.const 1
    i32.and
    if (result i32)
      get_local 3
    else
      get_local 1
      i32.load
      i32.const 0
      i32.eq
      if (result i32)
        get_global 0
        i32.const 0
        i32.add
        i32.const 69
        i32.store8
        get_global 0
        i32.const 1
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 2
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 3
        i32.add
        i32.const 111
        i32.store8
        get_global 0
        i32.const 4
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 5
        i32.add
        i32.const 58
        i32.store8
        get_global 0
        i32.const 6
        i32.add
        i32.const 32
        i32.store8
        get_global 0
        i32.const 7
        i32.add
        i32.const 0
        i32.store8
        get_global 0
        get_global 0
        i32.const 8
        i32.add
        set_global 0
        get_global 0
        i32.const 0
        i32.add
        i32.const 103
        i32.store8
        get_global 0
        i32.const 1
        i32.add
        i32.const 101
        i32.store8
        get_global 0
        i32.const 2
        i32.add
        i32.const 116
        i32.store8
        get_global 0
        i32.const 3
        i32.add
        i32.const 40
        i32.store8
        get_global 0
        i32.const 4
        i32.add
        i32.const 78
        i32.store8
        get_global 0
        i32.const 5
        i32.add
        i32.const 111
        i32.store8
        get_global 0
        i32.const 6
        i32.add
        i32.const 110
        i32.store8
        get_global 0
        i32.const 7
        i32.add
        i32.const 101
        i32.store8
        get_global 0
        i32.const 8
        i32.add
        i32.const 41
        i32.store8
        get_global 0
        i32.const 9
        i32.add
        i32.const 0
        i32.store8
        get_global 0
        i32.const 10
        i32.add
        i32.const 0
        i32.store8
        get_global 0
        i32.const 11
        i32.add
        i32.const 0
        i32.store8
        get_global 0
        get_global 0
        i32.const 12
        i32.add
        set_global 0
        call $String_concat
        call $Std_printString
        unreachable
      else
        get_global 0
        i32.const 0
        i32.add
        i32.const 77
        i32.store8
        get_global 0
        i32.const 1
        i32.add
        i32.const 97
        i32.store8
        get_global 0
        i32.const 2
        i32.add
        i32.const 116
        i32.store8
        get_global 0
        i32.const 3
        i32.add
        i32.const 99
        i32.store8
        get_global 0
        i32.const 4
        i32.add
        i32.const 104
        i32.store8
        get_global 0
        i32.const 5
        i32.add
        i32.const 32
        i32.store8
        get_global 0
        i32.const 6
        i32.add
        i32.const 101
        i32.store8
        get_global 0
        i32.const 7
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 8
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 9
        i32.add
        i32.const 111
        i32.store8
        get_global 0
        i32.const 10
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 11
        i32.add
        i32.const 0
        i32.store8
        get_global 0
        get_global 0
        i32.const 12
        i32.add
        set_global 0
        call $Std_printString
        unreachable
      end
    end
  )

  (func $O_getOrElse (param i32 i32) (result i32) (local i32 i32 i32)
    get_local 0
    set_local 2
    get_local 2
    i32.load
    i32.const 0
    i32.eq
    if (result i32)
      get_local 1
    else
      get_local 2
      i32.load
      i32.const 1
      i32.eq
      get_local 2
      i32.const 4
      i32.add
      i32.load
      set_local 3
      get_local 3
      set_local 4
      i32.const 1
      i32.and
      if (result i32)
        get_local 4
      else
        get_global 0
        i32.const 0
        i32.add
        i32.const 77
        i32.store8
        get_global 0
        i32.const 1
        i32.add
        i32.const 97
        i32.store8
        get_global 0
        i32.const 2
        i32.add
        i32.const 116
        i32.store8
        get_global 0
        i32.const 3
        i32.add
        i32.const 99
        i32.store8
        get_global 0
        i32.const 4
        i32.add
        i32.const 104
        i32.store8
        get_global 0
        i32.const 5
        i32.add
        i32.const 32
        i32.store8
        get_global 0
        i32.const 6
        i32.add
        i32.const 101
        i32.store8
        get_global 0
        i32.const 7
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 8
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 9
        i32.add
        i32.const 111
        i32.store8
        get_global 0
        i32.const 10
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 11
        i32.add
        i32.const 0
        i32.store8
        get_global 0
        get_global 0
        i32.const 12
        i32.add
        set_global 0
        call $Std_printString
        unreachable
      end
    end
  )

  (func $O_orElse (param i32 i32) (result i32) (local i32 i32)
    get_local 0
    set_local 2
    get_local 2
    i32.load
    i32.const 1
    i32.eq
    get_local 2
    i32.const 4
    i32.add
    i32.load
    set_local 3
    i32.const 1
    i32.and
    if (result i32)
      get_local 0
    else
      get_local 2
      i32.load
      i32.const 0
      i32.eq
      if (result i32)
        get_local 1
      else
        get_global 0
        i32.const 0
        i32.add
        i32.const 77
        i32.store8
        get_global 0
        i32.const 1
        i32.add
        i32.const 97
        i32.store8
        get_global 0
        i32.const 2
        i32.add
        i32.const 116
        i32.store8
        get_global 0
        i32.const 3
        i32.add
        i32.const 99
        i32.store8
        get_global 0
        i32.const 4
        i32.add
        i32.const 104
        i32.store8
        get_global 0
        i32.const 5
        i32.add
        i32.const 32
        i32.store8
        get_global 0
        i32.const 6
        i32.add
        i32.const 101
        i32.store8
        get_global 0
        i32.const 7
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 8
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 9
        i32.add
        i32.const 111
        i32.store8
        get_global 0
        i32.const 10
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 11
        i32.add
        i32.const 0
        i32.store8
        get_global 0
        get_global 0
        i32.const 12
        i32.add
        set_global 0
        call $Std_printString
        unreachable
      end
    end
  )

  (func $O_toList (param i32) (result i32) (local i32 i32 i32 i32 i32 i32 i32)
    get_local 0
    set_local 1
    get_local 1
    i32.load
    i32.const 1
    i32.eq
    get_local 1
    i32.const 4
    i32.add
    i32.load
    set_local 2
    get_local 2
    set_local 3
    i32.const 1
    i32.and
    if (result i32)
      get_global 0
      get_global 0
      set_local 5
      get_global 0
      i32.const 12
      i32.add
      set_global 0
      get_local 5
      i32.const 1
      i32.store
      get_local 5
      i32.const 4
      i32.add
      get_local 3
      i32.store
      get_local 5
      i32.const 8
      i32.add
      get_global 0
      set_local 6
      get_global 0
      i32.const 4
      i32.add
      set_global 0
      get_local 6
      i32.const 0
      i32.store
      get_local 6
      i32.store
      get_local 5
      i32.store
      get_global 0
      i32.const 4
      i32.add
      set_global 0
      i32.const -1
    else
      get_local 1
      i32.load
      i32.const 0
      i32.eq
      if (result i32)
        get_global 0
        get_global 0
        set_local 7
        get_global 0
        i32.const 4
        i32.add
        set_global 0
        get_local 7
        i32.const 0
        i32.store
        get_local 7
        i32.store
        get_global 0
        i32.const 4
        i32.add
        set_global 0
        i32.const -1
      else
        get_global 0
        i32.const 0
        i32.add
        i32.const 77
        i32.store8
        get_global 0
        i32.const 1
        i32.add
        i32.const 97
        i32.store8
        get_global 0
        i32.const 2
        i32.add
        i32.const 116
        i32.store8
        get_global 0
        i32.const 3
        i32.add
        i32.const 99
        i32.store8
        get_global 0
        i32.const 4
        i32.add
        i32.const 104
        i32.store8
        get_global 0
        i32.const 5
        i32.add
        i32.const 32
        i32.store8
        get_global 0
        i32.const 6
        i32.add
        i32.const 101
        i32.store8
        get_global 0
        i32.const 7
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 8
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 9
        i32.add
        i32.const 111
        i32.store8
        get_global 0
        i32.const 10
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 11
        i32.add
        i32.const 0
        i32.store8
        get_global 0
        get_global 0
        i32.const 12
        i32.add
        set_global 0
        call $Std_printString
        unreachable
      end
    end
  )

  (func $L_isEmpty (param i32) (result i32) (local i32)
    get_local 0
    set_local 1
    get_local 1
    i32.load
    i32.const 0
    i32.eq
    if (result i32)
      i32.const 1
    else
      i32.const 1
      if (result i32)
        i32.const 0
      else
        get_global 0
        i32.const 0
        i32.add
        i32.const 77
        i32.store8
        get_global 0
        i32.const 1
        i32.add
        i32.const 97
        i32.store8
        get_global 0
        i32.const 2
        i32.add
        i32.const 116
        i32.store8
        get_global 0
        i32.const 3
        i32.add
        i32.const 99
        i32.store8
        get_global 0
        i32.const 4
        i32.add
        i32.const 104
        i32.store8
        get_global 0
        i32.const 5
        i32.add
        i32.const 32
        i32.store8
        get_global 0
        i32.const 6
        i32.add
        i32.const 101
        i32.store8
        get_global 0
        i32.const 7
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 8
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 9
        i32.add
        i32.const 111
        i32.store8
        get_global 0
        i32.const 10
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 11
        i32.add
        i32.const 0
        i32.store8
        get_global 0
        get_global 0
        i32.const 12
        i32.add
        set_global 0
        call $Std_printString
        unreachable
      end
    end
  )

  (func $L_length (param i32) (result i32) (local i32 i32 i32 i32)
    get_local 0
    set_local 1
    get_local 1
    i32.load
    i32.const 0
    i32.eq
    if (result i32)
      i32.const 0
    else
      get_local 1
      i32.load
      i32.const 1
      i32.eq
      get_local 1
      i32.const 4
      i32.add
      i32.load
      set_local 2
      i32.const 1
      i32.and
      get_local 1
      i32.const 8
      i32.add
      i32.load
      set_local 3
      get_local 3
      set_local 4
      i32.const 1
      i32.and
      if (result i32)
        i32.const 1
        get_local 4
        call $L_length
        i32.add
      else
        get_global 0
        i32.const 0
        i32.add
        i32.const 77
        i32.store8
        get_global 0
        i32.const 1
        i32.add
        i32.const 97
        i32.store8
        get_global 0
        i32.const 2
        i32.add
        i32.const 116
        i32.store8
        get_global 0
        i32.const 3
        i32.add
        i32.const 99
        i32.store8
        get_global 0
        i32.const 4
        i32.add
        i32.const 104
        i32.store8
        get_global 0
        i32.const 5
        i32.add
        i32.const 32
        i32.store8
        get_global 0
        i32.const 6
        i32.add
        i32.const 101
        i32.store8
        get_global 0
        i32.const 7
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 8
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 9
        i32.add
        i32.const 111
        i32.store8
        get_global 0
        i32.const 10
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 11
        i32.add
        i32.const 0
        i32.store8
        get_global 0
        get_global 0
        i32.const 12
        i32.add
        set_global 0
        call $Std_printString
        unreachable
      end
    end
  )

  (func $L_head (param i32) (result i32) (local i32 i32 i32 i32)
    get_local 0
    set_local 1
    get_local 1
    i32.load
    i32.const 1
    i32.eq
    get_local 1
    i32.const 4
    i32.add
    i32.load
    set_local 2
    get_local 2
    set_local 3
    i32.const 1
    i32.and
    get_local 1
    i32.const 8
    i32.add
    i32.load
    set_local 4
    i32.const 1
    i32.and
    if (result i32)
      get_local 3
    else
      get_local 1
      i32.load
      i32.const 0
      i32.eq
      if (result i32)
        get_global 0
        i32.const 0
        i32.add
        i32.const 69
        i32.store8
        get_global 0
        i32.const 1
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 2
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 3
        i32.add
        i32.const 111
        i32.store8
        get_global 0
        i32.const 4
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 5
        i32.add
        i32.const 58
        i32.store8
        get_global 0
        i32.const 6
        i32.add
        i32.const 32
        i32.store8
        get_global 0
        i32.const 7
        i32.add
        i32.const 0
        i32.store8
        get_global 0
        get_global 0
        i32.const 8
        i32.add
        set_global 0
        get_global 0
        i32.const 0
        i32.add
        i32.const 104
        i32.store8
        get_global 0
        i32.const 1
        i32.add
        i32.const 101
        i32.store8
        get_global 0
        i32.const 2
        i32.add
        i32.const 97
        i32.store8
        get_global 0
        i32.const 3
        i32.add
        i32.const 100
        i32.store8
        get_global 0
        i32.const 4
        i32.add
        i32.const 40
        i32.store8
        get_global 0
        i32.const 5
        i32.add
        i32.const 78
        i32.store8
        get_global 0
        i32.const 6
        i32.add
        i32.const 105
        i32.store8
        get_global 0
        i32.const 7
        i32.add
        i32.const 108
        i32.store8
        get_global 0
        i32.const 8
        i32.add
        i32.const 41
        i32.store8
        get_global 0
        i32.const 9
        i32.add
        i32.const 0
        i32.store8
        get_global 0
        i32.const 10
        i32.add
        i32.const 0
        i32.store8
        get_global 0
        i32.const 11
        i32.add
        i32.const 0
        i32.store8
        get_global 0
        get_global 0
        i32.const 12
        i32.add
        set_global 0
        call $String_concat
        call $Std_printString
        unreachable
      else
        get_global 0
        i32.const 0
        i32.add
        i32.const 77
        i32.store8
        get_global 0
        i32.const 1
        i32.add
        i32.const 97
        i32.store8
        get_global 0
        i32.const 2
        i32.add
        i32.const 116
        i32.store8
        get_global 0
        i32.const 3
        i32.add
        i32.const 99
        i32.store8
        get_global 0
        i32.const 4
        i32.add
        i32.const 104
        i32.store8
        get_global 0
        i32.const 5
        i32.add
        i32.const 32
        i32.store8
        get_global 0
        i32.const 6
        i32.add
        i32.const 101
        i32.store8
        get_global 0
        i32.const 7
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 8
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 9
        i32.add
        i32.const 111
        i32.store8
        get_global 0
        i32.const 10
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 11
        i32.add
        i32.const 0
        i32.store8
        get_global 0
        get_global 0
        i32.const 12
        i32.add
        set_global 0
        call $Std_printString
        unreachable
      end
    end
  )

  (func $L_headOption (param i32) (result i32) (local i32 i32 i32 i32 i32 i32)
    get_local 0
    set_local 1
    get_local 1
    i32.load
    i32.const 1
    i32.eq
    get_local 1
    i32.const 4
    i32.add
    i32.load
    set_local 2
    get_local 2
    set_local 3
    i32.const 1
    i32.and
    get_local 1
    i32.const 8
    i32.add
    i32.load
    set_local 4
    i32.const 1
    i32.and
    if (result i32)
      get_global 0
      get_global 0
      set_local 5
      get_global 0
      i32.const 8
      i32.add
      set_global 0
      get_local 5
      i32.const 1
      i32.store
      get_local 5
      i32.const 4
      i32.add
      get_local 3
      i32.store
      get_local 5
      i32.store
      get_global 0
      i32.const 4
      i32.add
      set_global 0
      i32.const -1
    else
      get_local 1
      i32.load
      i32.const 0
      i32.eq
      if (result i32)
        get_global 0
        get_global 0
        set_local 6
        get_global 0
        i32.const 4
        i32.add
        set_global 0
        get_local 6
        i32.const 0
        i32.store
        get_local 6
        i32.store
        get_global 0
        i32.const 4
        i32.add
        set_global 0
        i32.const -1
      else
        get_global 0
        i32.const 0
        i32.add
        i32.const 77
        i32.store8
        get_global 0
        i32.const 1
        i32.add
        i32.const 97
        i32.store8
        get_global 0
        i32.const 2
        i32.add
        i32.const 116
        i32.store8
        get_global 0
        i32.const 3
        i32.add
        i32.const 99
        i32.store8
        get_global 0
        i32.const 4
        i32.add
        i32.const 104
        i32.store8
        get_global 0
        i32.const 5
        i32.add
        i32.const 32
        i32.store8
        get_global 0
        i32.const 6
        i32.add
        i32.const 101
        i32.store8
        get_global 0
        i32.const 7
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 8
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 9
        i32.add
        i32.const 111
        i32.store8
        get_global 0
        i32.const 10
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 11
        i32.add
        i32.const 0
        i32.store8
        get_global 0
        get_global 0
        i32.const 12
        i32.add
        set_global 0
        call $Std_printString
        unreachable
      end
    end
  )

  (func $L_reverse (param i32) (result i32) (local i32 i32)
    get_global 0
    set_local 1
    get_global 0
    i32.const 12
    i32.add
    set_global 0
    get_local 1
    i32.const 10
    i32.store
    get_local 1
    i32.const 4
    i32.add
    get_local 0
    i32.store
    get_local 1
    i32.const 8
    i32.add
    get_global 0
    set_local 2
    get_global 0
    i32.const 4
    i32.add
    set_global 0
    get_local 2
    i32.const 0
    i32.store
    get_local 2
    i32.store
    get_local 1
  )

  (func $L_reverseAcc (param i32 i32) (result i32) (local i32 i32 i32 i32 i32 i32)
    loop $tailjump (result i32)
      get_local 0
      set_local 2
      get_local 2
      i32.load
      i32.const 0
      i32.eq
      if (result i32)
        get_local 1
      else
        get_local 2
        i32.load
        i32.const 1
        i32.eq
        get_local 2
        i32.const 4
        i32.add
        i32.load
        set_local 3
        get_local 3
        set_local 4
        i32.const 1
        i32.and
        get_local 2
        i32.const 8
        i32.add
        i32.load
        set_local 5
        get_local 5
        set_local 6
        i32.const 1
        i32.and
        if (result i32)
          get_local 6
          get_global 0
          set_local 7
          get_global 0
          i32.const 12
          i32.add
          set_global 0
          get_local 7
          i32.const 1
          i32.store
          get_local 7
          i32.const 4
          i32.add
          get_local 4
          i32.store
          get_local 7
          i32.const 8
          i32.add
          get_local 1
          i32.store
          get_local 7
          set_local 1
          set_local 0
          br $tailjump
        else
          get_global 0
          i32.const 0
          i32.add
          i32.const 77
          i32.store8
          get_global 0
          i32.const 1
          i32.add
          i32.const 97
          i32.store8
          get_global 0
          i32.const 2
          i32.add
          i32.const 116
          i32.store8
          get_global 0
          i32.const 3
          i32.add
          i32.const 99
          i32.store8
          get_global 0
          i32.const 4
          i32.add
          i32.const 104
          i32.store8
          get_global 0
          i32.const 5
          i32.add
          i32.const 32
          i32.store8
          get_global 0
          i32.const 6
          i32.add
          i32.const 101
          i32.store8
          get_global 0
          i32.const 7
          i32.add
          i32.const 114
          i32.store8
          get_global 0
          i32.const 8
          i32.add
          i32.const 114
          i32.store8
          get_global 0
          i32.const 9
          i32.add
          i32.const 111
          i32.store8
          get_global 0
          i32.const 10
          i32.add
          i32.const 114
          i32.store8
          get_global 0
          i32.const 11
          i32.add
          i32.const 0
          i32.store8
          get_global 0
          get_global 0
          i32.const 12
          i32.add
          set_global 0
          call $Std_printString
          unreachable
        end
      end
    end
  )

  (func $L_indexOf (param i32 i32) (result i32) (local i32 i32 i32 i32 i32 i32)
    get_local 0
    set_local 2
    get_local 2
    i32.load
    i32.const 0
    i32.eq
    if (result i32)
      i32.const 0
      i32.const 1
      i32.sub
    else
      get_local 2
      i32.load
      i32.const 1
      i32.eq
      get_local 2
      i32.const 4
      i32.add
      i32.load
      set_local 3
      get_local 3
      set_local 4
      i32.const 1
      i32.and
      get_local 2
      i32.const 8
      i32.add
      i32.load
      set_local 5
      get_local 5
      set_local 6
      i32.const 1
      i32.and
      if (result i32)
        get_local 4
        get_local 1
        i32.eq
        if (result i32)
          i32.const 0
        else
          get_local 6
          get_local 1
          call $L_indexOf
          set_local 7
          i32.const 0
          get_local 7
          i32.le_s
          if (result i32)
            get_local 7
            i32.const 1
            i32.add
          else
            i32.const 0
            i32.const 1
            i32.sub
          end
        end
      else
        get_global 0
        i32.const 0
        i32.add
        i32.const 77
        i32.store8
        get_global 0
        i32.const 1
        i32.add
        i32.const 97
        i32.store8
        get_global 0
        i32.const 2
        i32.add
        i32.const 116
        i32.store8
        get_global 0
        i32.const 3
        i32.add
        i32.const 99
        i32.store8
        get_global 0
        i32.const 4
        i32.add
        i32.const 104
        i32.store8
        get_global 0
        i32.const 5
        i32.add
        i32.const 32
        i32.store8
        get_global 0
        i32.const 6
        i32.add
        i32.const 101
        i32.store8
        get_global 0
        i32.const 7
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 8
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 9
        i32.add
        i32.const 111
        i32.store8
        get_global 0
        i32.const 10
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 11
        i32.add
        i32.const 0
        i32.store8
        get_global 0
        get_global 0
        i32.const 12
        i32.add
        set_global 0
        call $Std_printString
        unreachable
      end
    end
  )

  (func $L_range (param i32 i32) (result i32) (local i32 i32)
    get_local 1
    get_local 0
    i32.lt_s
    if (result i32)
      get_global 0
      set_local 2
      get_global 0
      i32.const 4
      i32.add
      set_global 0
      get_local 2
      i32.const 0
      i32.store
      get_local 2
    else
      get_global 0
      set_local 3
      get_global 0
      i32.const 12
      i32.add
      set_global 0
      get_local 3
      i32.const 1
      i32.store
      get_local 3
      i32.const 4
      i32.add
      get_local 0
      i32.store
      get_local 3
      i32.const 8
      i32.add
      get_local 0
      i32.const 1
      i32.add
      get_local 1
      call $L_range
      i32.store
      get_local 3
    end
  )

  (func $L_sum (param i32) (result i32) (local i32 i32 i32 i32 i32)
    get_local 0
    set_local 1
    get_local 1
    i32.load
    i32.const 0
    i32.eq
    if (result i32)
      i32.const 0
    else
      get_local 1
      i32.load
      i32.const 1
      i32.eq
      get_local 1
      i32.const 4
      i32.add
      i32.load
      set_local 2
      get_local 2
      set_local 3
      i32.const 1
      i32.and
      get_local 1
      i32.const 8
      i32.add
      i32.load
      set_local 4
      get_local 4
      set_local 5
      i32.const 1
      i32.and
      if (result i32)
        get_local 3
        get_local 5
        call $L_sum
        i32.add
      else
        get_global 0
        i32.const 0
        i32.add
        i32.const 77
        i32.store8
        get_global 0
        i32.const 1
        i32.add
        i32.const 97
        i32.store8
        get_global 0
        i32.const 2
        i32.add
        i32.const 116
        i32.store8
        get_global 0
        i32.const 3
        i32.add
        i32.const 99
        i32.store8
        get_global 0
        i32.const 4
        i32.add
        i32.const 104
        i32.store8
        get_global 0
        i32.const 5
        i32.add
        i32.const 32
        i32.store8
        get_global 0
        i32.const 6
        i32.add
        i32.const 101
        i32.store8
        get_global 0
        i32.const 7
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 8
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 9
        i32.add
        i32.const 111
        i32.store8
        get_global 0
        i32.const 10
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 11
        i32.add
        i32.const 0
        i32.store8
        get_global 0
        get_global 0
        i32.const 12
        i32.add
        set_global 0
        call $Std_printString
        unreachable
      end
    end
  )

  (func $L_concat (param i32 i32) (result i32) (local i32 i32 i32 i32 i32 i32)
    get_local 0
    set_local 2
    get_local 2
    i32.load
    i32.const 0
    i32.eq
    if (result i32)
      get_local 1
    else
      get_local 2
      i32.load
      i32.const 1
      i32.eq
      get_local 2
      i32.const 4
      i32.add
      i32.load
      set_local 3
      get_local 3
      set_local 4
      i32.const 1
      i32.and
      get_local 2
      i32.const 8
      i32.add
      i32.load
      set_local 5
      get_local 5
      set_local 6
      i32.const 1
      i32.and
      if (result i32)
        get_global 0
        set_local 7
        get_global 0
        i32.const 12
        i32.add
        set_global 0
        get_local 7
        i32.const 1
        i32.store
        get_local 7
        i32.const 4
        i32.add
        get_local 4
        i32.store
        get_local 7
        i32.const 8
        i32.add
        get_local 6
        get_local 1
        call $L_concat
        i32.store
        get_local 7
      else
        get_global 0
        i32.const 0
        i32.add
        i32.const 77
        i32.store8
        get_global 0
        i32.const 1
        i32.add
        i32.const 97
        i32.store8
        get_global 0
        i32.const 2
        i32.add
        i32.const 116
        i32.store8
        get_global 0
        i32.const 3
        i32.add
        i32.const 99
        i32.store8
        get_global 0
        i32.const 4
        i32.add
        i32.const 104
        i32.store8
        get_global 0
        i32.const 5
        i32.add
        i32.const 32
        i32.store8
        get_global 0
        i32.const 6
        i32.add
        i32.const 101
        i32.store8
        get_global 0
        i32.const 7
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 8
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 9
        i32.add
        i32.const 111
        i32.store8
        get_global 0
        i32.const 10
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 11
        i32.add
        i32.const 0
        i32.store8
        get_global 0
        get_global 0
        i32.const 12
        i32.add
        set_global 0
        call $Std_printString
        unreachable
      end
    end
  )

  (func $L_contains (param i32 i32) (result i32) (local i32 i32 i32 i32 i32)
    get_local 0
    set_local 2
    get_local 2
    i32.load
    i32.const 0
    i32.eq
    if (result i32)
      i32.const 0
    else
      get_local 2
      i32.load
      i32.const 1
      i32.eq
      get_local 2
      i32.const 4
      i32.add
      i32.load
      set_local 3
      get_local 3
      set_local 4
      i32.const 1
      i32.and
      get_local 2
      i32.const 8
      i32.add
      i32.load
      set_local 5
      get_local 5
      set_local 6
      i32.const 1
      i32.and
      if (result i32)
        get_local 4
        get_local 1
        i32.eq
        if (result i32)
          i32.const 1
        else
          get_local 6
          get_local 1
          call $L_contains
        end
      else
        get_global 0
        i32.const 0
        i32.add
        i32.const 77
        i32.store8
        get_global 0
        i32.const 1
        i32.add
        i32.const 97
        i32.store8
        get_global 0
        i32.const 2
        i32.add
        i32.const 116
        i32.store8
        get_global 0
        i32.const 3
        i32.add
        i32.const 99
        i32.store8
        get_global 0
        i32.const 4
        i32.add
        i32.const 104
        i32.store8
        get_global 0
        i32.const 5
        i32.add
        i32.const 32
        i32.store8
        get_global 0
        i32.const 6
        i32.add
        i32.const 101
        i32.store8
        get_global 0
        i32.const 7
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 8
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 9
        i32.add
        i32.const 111
        i32.store8
        get_global 0
        i32.const 10
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 11
        i32.add
        i32.const 0
        i32.store8
        get_global 0
        get_global 0
        i32.const 12
        i32.add
        set_global 0
        call $Std_printString
        unreachable
      end
    end
  )

  (func $L_merge (param i32 i32) (result i32) (local i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32)
    get_local 0
    set_local 2
    get_local 2
    i32.load
    i32.const 0
    i32.eq
    if (result i32)
      get_local 1
    else
      get_local 2
      i32.load
      i32.const 1
      i32.eq
      get_local 2
      i32.const 4
      i32.add
      i32.load
      set_local 3
      get_local 3
      set_local 4
      i32.const 1
      i32.and
      get_local 2
      i32.const 8
      i32.add
      i32.load
      set_local 5
      get_local 5
      set_local 6
      i32.const 1
      i32.and
      if (result i32)
        get_local 1
        set_local 7
        get_local 7
        i32.load
        i32.const 0
        i32.eq
        if (result i32)
          get_local 0
        else
          get_local 7
          i32.load
          i32.const 1
          i32.eq
          get_local 7
          i32.const 4
          i32.add
          i32.load
          set_local 8
          get_local 8
          set_local 9
          i32.const 1
          i32.and
          get_local 7
          i32.const 8
          i32.add
          i32.load
          set_local 10
          get_local 10
          set_local 11
          i32.const 1
          i32.and
          if (result i32)
            get_local 4
            get_local 9
            i32.le_s
            if (result i32)
              get_global 0
              set_local 12
              get_global 0
              i32.const 12
              i32.add
              set_global 0
              get_local 12
              i32.const 1
              i32.store
              get_local 12
              i32.const 4
              i32.add
              get_local 4
              i32.store
              get_local 12
              i32.const 8
              i32.add
              get_local 6
              get_local 1
              call $L_merge
              i32.store
              get_local 12
            else
              get_global 0
              set_local 13
              get_global 0
              i32.const 12
              i32.add
              set_global 0
              get_local 13
              i32.const 1
              i32.store
              get_local 13
              i32.const 4
              i32.add
              get_local 9
              i32.store
              get_local 13
              i32.const 8
              i32.add
              get_local 0
              get_local 11
              call $L_merge
              i32.store
              get_local 13
            end
          else
            get_global 0
            i32.const 0
            i32.add
            i32.const 77
            i32.store8
            get_global 0
            i32.const 1
            i32.add
            i32.const 97
            i32.store8
            get_global 0
            i32.const 2
            i32.add
            i32.const 116
            i32.store8
            get_global 0
            i32.const 3
            i32.add
            i32.const 99
            i32.store8
            get_global 0
            i32.const 4
            i32.add
            i32.const 104
            i32.store8
            get_global 0
            i32.const 5
            i32.add
            i32.const 32
            i32.store8
            get_global 0
            i32.const 6
            i32.add
            i32.const 101
            i32.store8
            get_global 0
            i32.const 7
            i32.add
            i32.const 114
            i32.store8
            get_global 0
            i32.const 8
            i32.add
            i32.const 114
            i32.store8
            get_global 0
            i32.const 9
            i32.add
            i32.const 111
            i32.store8
            get_global 0
            i32.const 10
            i32.add
            i32.const 114
            i32.store8
            get_global 0
            i32.const 11
            i32.add
            i32.const 0
            i32.store8
            get_global 0
            get_global 0
            i32.const 12
            i32.add
            set_global 0
            call $Std_printString
            unreachable
          end
        end
      else
        get_global 0
        i32.const 0
        i32.add
        i32.const 77
        i32.store8
        get_global 0
        i32.const 1
        i32.add
        i32.const 97
        i32.store8
        get_global 0
        i32.const 2
        i32.add
        i32.const 116
        i32.store8
        get_global 0
        i32.const 3
        i32.add
        i32.const 99
        i32.store8
        get_global 0
        i32.const 4
        i32.add
        i32.const 104
        i32.store8
        get_global 0
        i32.const 5
        i32.add
        i32.const 32
        i32.store8
        get_global 0
        i32.const 6
        i32.add
        i32.const 101
        i32.store8
        get_global 0
        i32.const 7
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 8
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 9
        i32.add
        i32.const 111
        i32.store8
        get_global 0
        i32.const 10
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 11
        i32.add
        i32.const 0
        i32.store8
        get_global 0
        get_global 0
        i32.const 12
        i32.add
        set_global 0
        call $Std_printString
        unreachable
      end
    end
  )

  (func $L_split (param i32) (result i32) (local i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32)
    get_local 0
    set_local 1
    get_local 1
    i32.load
    i32.const 1
    i32.eq
    get_local 1
    i32.const 4
    i32.add
    i32.load
    set_local 2
    get_local 2
    set_local 3
    i32.const 1
    i32.and
    get_local 1
    i32.const 8
    i32.add
    i32.load
    set_local 4
    get_local 4
    i32.load
    i32.const 1
    i32.eq
    get_local 4
    i32.const 4
    i32.add
    i32.load
    set_local 5
    get_local 5
    set_local 6
    i32.const 1
    i32.and
    get_local 4
    i32.const 8
    i32.add
    i32.load
    set_local 7
    get_local 7
    set_local 8
    i32.const 1
    i32.and
    i32.and
    if (result i32)
      get_local 8
      call $L_split
      set_local 9
      get_local 9
      set_local 10
      get_local 10
      i32.load
      i32.const 0
      i32.eq
      get_local 10
      i32.const 4
      i32.add
      i32.load
      set_local 11
      get_local 11
      set_local 12
      i32.const 1
      i32.and
      get_local 10
      i32.const 8
      i32.add
      i32.load
      set_local 13
      get_local 13
      set_local 14
      i32.const 1
      i32.and
      if (result i32)
        get_global 0
        get_global 0
        set_local 17
        get_global 0
        i32.const 12
        i32.add
        set_global 0
        get_local 17
        i32.const 0
        i32.store
        get_local 17
        i32.const 4
        i32.add
        get_global 0
        set_local 18
        get_global 0
        i32.const 12
        i32.add
        set_global 0
        get_local 18
        i32.const 1
        i32.store
        get_local 18
        i32.const 4
        i32.add
        get_local 3
        i32.store
        get_local 18
        i32.const 8
        i32.add
        get_local 12
        i32.store
        get_local 18
        i32.store
        get_local 17
        i32.const 8
        i32.add
        get_global 0
        set_local 19
        get_global 0
        i32.const 12
        i32.add
        set_global 0
        get_local 19
        i32.const 1
        i32.store
        get_local 19
        i32.const 4
        i32.add
        get_local 6
        i32.store
        get_local 19
        i32.const 8
        i32.add
        get_local 14
        i32.store
        get_local 19
        i32.store
        get_local 17
        i32.store
        get_global 0
        i32.const 4
        i32.add
        set_global 0
        i32.const -1
      else
        get_global 0
        i32.const 0
        i32.add
        i32.const 77
        i32.store8
        get_global 0
        i32.const 1
        i32.add
        i32.const 97
        i32.store8
        get_global 0
        i32.const 2
        i32.add
        i32.const 116
        i32.store8
        get_global 0
        i32.const 3
        i32.add
        i32.const 99
        i32.store8
        get_global 0
        i32.const 4
        i32.add
        i32.const 104
        i32.store8
        get_global 0
        i32.const 5
        i32.add
        i32.const 32
        i32.store8
        get_global 0
        i32.const 6
        i32.add
        i32.const 101
        i32.store8
        get_global 0
        i32.const 7
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 8
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 9
        i32.add
        i32.const 111
        i32.store8
        get_global 0
        i32.const 10
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 11
        i32.add
        i32.const 0
        i32.store8
        get_global 0
        get_global 0
        i32.const 12
        i32.add
        set_global 0
        call $Std_printString
        unreachable
      end
    else
      i32.const 1
      if (result i32)
        get_global 0
        get_global 0
        set_local 21
        get_global 0
        i32.const 12
        i32.add
        set_global 0
        get_local 21
        i32.const 0
        i32.store
        get_local 21
        i32.const 4
        i32.add
        get_local 0
        i32.store
        get_local 21
        i32.const 8
        i32.add
        get_global 0
        set_local 22
        get_global 0
        i32.const 4
        i32.add
        set_global 0
        get_local 22
        i32.const 0
        i32.store
        get_local 22
        i32.store
        get_local 21
        i32.store
        get_global 0
        i32.const 4
        i32.add
        set_global 0
        i32.const -1
      else
        get_global 0
        i32.const 0
        i32.add
        i32.const 77
        i32.store8
        get_global 0
        i32.const 1
        i32.add
        i32.const 97
        i32.store8
        get_global 0
        i32.const 2
        i32.add
        i32.const 116
        i32.store8
        get_global 0
        i32.const 3
        i32.add
        i32.const 99
        i32.store8
        get_global 0
        i32.const 4
        i32.add
        i32.const 104
        i32.store8
        get_global 0
        i32.const 5
        i32.add
        i32.const 32
        i32.store8
        get_global 0
        i32.const 6
        i32.add
        i32.const 101
        i32.store8
        get_global 0
        i32.const 7
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 8
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 9
        i32.add
        i32.const 111
        i32.store8
        get_global 0
        i32.const 10
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 11
        i32.add
        i32.const 0
        i32.store8
        get_global 0
        get_global 0
        i32.const 12
        i32.add
        set_global 0
        call $Std_printString
        unreachable
      end
    end
  )

  (func $L_mergeSort (param i32) (result i32) (local i32 i32 i32 i32 i32 i32 i32 i32 i32 i32 i32)
    get_local 0
    set_local 1
    get_local 1
    i32.load
    i32.const 0
    i32.eq
    if (result i32)
      get_global 0
      get_local 0
      i32.store
      get_global 0
      i32.const 4
      i32.add
      set_global 0
      i32.const -1
    else
      get_local 1
      i32.load
      i32.const 1
      i32.eq
      get_local 1
      i32.const 4
      i32.add
      i32.load
      set_local 2
      get_local 2
      set_local 3
      i32.const 1
      i32.and
      get_local 1
      i32.const 8
      i32.add
      i32.load
      set_local 4
      get_local 4
      i32.load
      i32.const 0
      i32.eq
      i32.and
      if (result i32)
        get_global 0
        get_local 0
        i32.store
        get_global 0
        i32.const 4
        i32.add
        set_global 0
        i32.const -1
      else
        get_local 1
        set_local 5
        i32.const 1
        if (result i32)
          get_local 5
          call $L_split
          set_local 6
          get_local 6
          i32.load
          i32.const 0
          i32.eq
          get_local 6
          i32.const 4
          i32.add
          i32.load
          set_local 7
          get_local 7
          set_local 8
          i32.const 1
          i32.and
          get_local 6
          i32.const 8
          i32.add
          i32.load
          set_local 9
          get_local 9
          set_local 10
          i32.const 1
          i32.and
          if (result i32)
            get_global 0
            set_local 11
            get_global 0
            i32.const 12
            i32.add
            set_global 0
            get_local 11
            i32.const 16
            i32.store
            get_local 11
            i32.const 4
            i32.add
            get_local 8
            call $L_mergeSort
            i32.store
            get_local 11
            i32.const 8
            i32.add
            get_local 10
            call $L_mergeSort
            i32.store
            get_local 11
          else
            get_global 0
            i32.const 0
            i32.add
            i32.const 77
            i32.store8
            get_global 0
            i32.const 1
            i32.add
            i32.const 97
            i32.store8
            get_global 0
            i32.const 2
            i32.add
            i32.const 116
            i32.store8
            get_global 0
            i32.const 3
            i32.add
            i32.const 99
            i32.store8
            get_global 0
            i32.const 4
            i32.add
            i32.const 104
            i32.store8
            get_global 0
            i32.const 5
            i32.add
            i32.const 32
            i32.store8
            get_global 0
            i32.const 6
            i32.add
            i32.const 101
            i32.store8
            get_global 0
            i32.const 7
            i32.add
            i32.const 114
            i32.store8
            get_global 0
            i32.const 8
            i32.add
            i32.const 114
            i32.store8
            get_global 0
            i32.const 9
            i32.add
            i32.const 111
            i32.store8
            get_global 0
            i32.const 10
            i32.add
            i32.const 114
            i32.store8
            get_global 0
            i32.const 11
            i32.add
            i32.const 0
            i32.store8
            get_global 0
            get_global 0
            i32.const 12
            i32.add
            set_global 0
            call $Std_printString
            unreachable
          end
        else
          get_global 0
          i32.const 0
          i32.add
          i32.const 77
          i32.store8
          get_global 0
          i32.const 1
          i32.add
          i32.const 97
          i32.store8
          get_global 0
          i32.const 2
          i32.add
          i32.const 116
          i32.store8
          get_global 0
          i32.const 3
          i32.add
          i32.const 99
          i32.store8
          get_global 0
          i32.const 4
          i32.add
          i32.const 104
          i32.store8
          get_global 0
          i32.const 5
          i32.add
          i32.const 32
          i32.store8
          get_global 0
          i32.const 6
          i32.add
          i32.const 101
          i32.store8
          get_global 0
          i32.const 7
          i32.add
          i32.const 114
          i32.store8
          get_global 0
          i32.const 8
          i32.add
          i32.const 114
          i32.store8
          get_global 0
          i32.const 9
          i32.add
          i32.const 111
          i32.store8
          get_global 0
          i32.const 10
          i32.add
          i32.const 114
          i32.store8
          get_global 0
          i32.const 11
          i32.add
          i32.const 0
          i32.store8
          get_global 0
          get_global 0
          i32.const 12
          i32.add
          set_global 0
          call $Std_printString
          unreachable
        end
      end
    end
  )

  (func $L_toString (param i32) (result i32) (local i32 i32)
    get_local 0
    set_local 1
    get_local 1
    i32.load
    i32.const 0
    i32.eq
    if (result i32)
      get_global 0
      i32.const 0
      i32.add
      i32.const 76
      i32.store8
      get_global 0
      i32.const 1
      i32.add
      i32.const 105
      i32.store8
      get_global 0
      i32.const 2
      i32.add
      i32.const 115
      i32.store8
      get_global 0
      i32.const 3
      i32.add
      i32.const 116
      i32.store8
      get_global 0
      i32.const 4
      i32.add
      i32.const 40
      i32.store8
      get_global 0
      i32.const 5
      i32.add
      i32.const 41
      i32.store8
      get_global 0
      i32.const 6
      i32.add
      i32.const 0
      i32.store8
      get_global 0
      i32.const 7
      i32.add
      i32.const 0
      i32.store8
      get_global 0
      get_global 0
      i32.const 8
      i32.add
      set_global 0
    else
      get_local 1
      set_local 2
      i32.const 1
      if (result i32)
        get_global 0
        i32.const 0
        i32.add
        i32.const 76
        i32.store8
        get_global 0
        i32.const 1
        i32.add
        i32.const 105
        i32.store8
        get_global 0
        i32.const 2
        i32.add
        i32.const 115
        i32.store8
        get_global 0
        i32.const 3
        i32.add
        i32.const 116
        i32.store8
        get_global 0
        i32.const 4
        i32.add
        i32.const 40
        i32.store8
        get_global 0
        i32.const 5
        i32.add
        i32.const 0
        i32.store8
        get_global 0
        i32.const 6
        i32.add
        i32.const 0
        i32.store8
        get_global 0
        i32.const 7
        i32.add
        i32.const 0
        i32.store8
        get_global 0
        get_global 0
        i32.const 8
        i32.add
        set_global 0
        get_local 2
        call $L_toString1
        call $String_concat
        get_global 0
        i32.const 0
        i32.add
        i32.const 41
        i32.store8
        get_global 0
        i32.const 1
        i32.add
        i32.const 0
        i32.store8
        get_global 0
        i32.const 2
        i32.add
        i32.const 0
        i32.store8
        get_global 0
        i32.const 3
        i32.add
        i32.const 0
        i32.store8
        get_global 0
        get_global 0
        i32.const 4
        i32.add
        set_global 0
        call $String_concat
      else
        get_global 0
        i32.const 0
        i32.add
        i32.const 77
        i32.store8
        get_global 0
        i32.const 1
        i32.add
        i32.const 97
        i32.store8
        get_global 0
        i32.const 2
        i32.add
        i32.const 116
        i32.store8
        get_global 0
        i32.const 3
        i32.add
        i32.const 99
        i32.store8
        get_global 0
        i32.const 4
        i32.add
        i32.const 104
        i32.store8
        get_global 0
        i32.const 5
        i32.add
        i32.const 32
        i32.store8
        get_global 0
        i32.const 6
        i32.add
        i32.const 101
        i32.store8
        get_global 0
        i32.const 7
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 8
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 9
        i32.add
        i32.const 111
        i32.store8
        get_global 0
        i32.const 10
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 11
        i32.add
        i32.const 0
        i32.store8
        get_global 0
        get_global 0
        i32.const 12
        i32.add
        set_global 0
        call $Std_printString
        unreachable
      end
    end
  )

  (func $L_toString1 (param i32) (result i32) (local i32 i32 i32 i32 i32 i32 i32 i32 i32)
    get_local 0
    set_local 1
    get_local 1
    i32.load
    i32.const 1
    i32.eq
    get_local 1
    i32.const 4
    i32.add
    i32.load
    set_local 2
    get_local 2
    set_local 3
    i32.const 1
    i32.and
    get_local 1
    i32.const 8
    i32.add
    i32.load
    set_local 4
    get_local 4
    i32.load
    i32.const 0
    i32.eq
    i32.and
    if (result i32)
      get_global 0
      set_local 5
      get_global 0
      i32.const 8
      i32.add
      set_global 0
      get_local 5
      i32.const -1
      i32.store
      get_local 5
      i32.const 4
      i32.add
      get_local 3
      i32.store
      get_local 5
    else
      get_local 1
      i32.load
      i32.const 1
      i32.eq
      get_local 1
      i32.const 4
      i32.add
      i32.load
      set_local 6
      get_local 6
      set_local 7
      i32.const 1
      i32.and
      get_local 1
      i32.const 8
      i32.add
      i32.load
      set_local 8
      get_local 8
      set_local 9
      i32.const 1
      i32.and
      if (result i32)
        get_global 0
        get_local 7
        call $Std_intToString
        get_global 0
        i32.const 0
        i32.add
        i32.const 44
        i32.store8
        get_global 0
        i32.const 1
        i32.add
        i32.const 32
        i32.store8
        get_global 0
        i32.const 2
        i32.add
        i32.const 0
        i32.store8
        get_global 0
        i32.const 3
        i32.add
        i32.const 0
        i32.store8
        get_global 0
        get_global 0
        i32.const 4
        i32.add
        set_global 0
        call $String_concat
        get_local 9
        call $L_toString1
        call $String_concat
        i32.store
        get_global 0
        i32.const 4
        i32.add
        set_global 0
        i32.const -1
      else
        get_global 0
        i32.const 0
        i32.add
        i32.const 77
        i32.store8
        get_global 0
        i32.const 1
        i32.add
        i32.const 97
        i32.store8
        get_global 0
        i32.const 2
        i32.add
        i32.const 116
        i32.store8
        get_global 0
        i32.const 3
        i32.add
        i32.const 99
        i32.store8
        get_global 0
        i32.const 4
        i32.add
        i32.const 104
        i32.store8
        get_global 0
        i32.const 5
        i32.add
        i32.const 32
        i32.store8
        get_global 0
        i32.const 6
        i32.add
        i32.const 101
        i32.store8
        get_global 0
        i32.const 7
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 8
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 9
        i32.add
        i32.const 111
        i32.store8
        get_global 0
        i32.const 10
        i32.add
        i32.const 114
        i32.store8
        get_global 0
        i32.const 11
        i32.add
        i32.const 0
        i32.store8
        get_global 0
        get_global 0
        i32.const 12
        i32.add
        set_global 0
        call $Std_printString
        unreachable
      end
    end
  )

  (func $L_take (param i32 i32) (result i32) (local i32 i32 i32 i32 i32 i32 i32 i32)
    get_local 1
    i32.const 0
    i32.le_s
    if (result i32)
      get_global 0
      set_local 2
      get_global 0
      i32.const 4
      i32.add
      set_global 0
      get_local 2
      i32.const 0
      i32.store
      get_local 2
    else
      get_local 0
      set_local 3
      get_local 3
      i32.load
      i32.const 0
      i32.eq
      if (result i32)
        get_global 0
        set_local 4
        get_global 0
        i32.const 4
        i32.add
        set_global 0
        get_local 4
        i32.const 0
        i32.store
        get_local 4
      else
        get_local 3
        i32.load
        i32.const 1
        i32.eq
        get_local 3
        i32.const 4
        i32.add
        i32.load
        set_local 5
        get_local 5
        set_local 6
        i32.const 1
        i32.and
        get_local 3
        i32.const 8
        i32.add
        i32.load
        set_local 7
        get_local 7
        set_local 8
        i32.const 1
        i32.and
        if (result i32)
          get_global 0
          set_local 9
          get_global 0
          i32.const 12
          i32.add
          set_global 0
          get_local 9
          i32.const 1
          i32.store
          get_local 9
          i32.const 4
          i32.add
          get_local 6
          i32.store
          get_local 9
          i32.const 8
          i32.add
          get_local 8
          get_local 1
          i32.const 1
          i32.sub
          call $L_take
          i32.store
          get_local 9
        else
          get_global 0
          i32.const 0
          i32.add
          i32.const 77
          i32.store8
          get_global 0
          i32.const 1
          i32.add
          i32.const 97
          i32.store8
          get_global 0
          i32.const 2
          i32.add
          i32.const 116
          i32.store8
          get_global 0
          i32.const 3
          i32.add
          i32.const 99
          i32.store8
          get_global 0
          i32.const 4
          i32.add
          i32.const 104
          i32.store8
          get_global 0
          i32.const 5
          i32.add
          i32.const 32
          i32.store8
          get_global 0
          i32.const 6
          i32.add
          i32.const 101
          i32.store8
          get_global 0
          i32.const 7
          i32.add
          i32.const 114
          i32.store8
          get_global 0
          i32.const 8
          i32.add
          i32.const 114
          i32.store8
          get_global 0
          i32.const 9
          i32.add
          i32.const 111
          i32.store8
          get_global 0
          i32.const 10
          i32.add
          i32.const 114
          i32.store8
          get_global 0
          i32.const 11
          i32.add
          i32.const 0
          i32.store8
          get_global 0
          get_global 0
          i32.const 12
          i32.add
          set_global 0
          call $Std_printString
          unreachable
        end
      end
    end
  )
)